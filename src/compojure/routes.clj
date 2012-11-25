(ns compojure.routes
  (:use compojure.core
        compojure.views
        [hiccup.middleware :only (wrap-base-url)])
  (:require [compojure.route :as route]
            [compojure.handler :as handler]
            [ring.util.response :as ring]
            [compojure.response :as response]
            [taoensso.carmine :as redis]
            [clj-json.core :as json]
            ))

(def redis-url (java.net.URI. (System/getenv "REDISTOGO_URL")))
(defn password [uri] (if (nil? (.getUserInfo uri)) nil (last (.split (.getUserInfo uri) ":"))))
(def pool (redis/make-conn-pool))
(def spec-server1 (redis/make-conn-spec :host (.getHost redis-url)
                    :port (.getPort redis-url)
                    :password (password redis-url)
                    :timeout 4000))

(defmacro wredis [& body] `(redis/with-conn pool spec-server1 ~@body))

(defn save-weight [weight]
  (let [data (wredis (redis/get "data"))
        new-entry {:date (.getTime (java.util.Date.)) :weight weight}
        updated-data (if (nil? data) (vector new-entry) (conj data new-entry))]
    (wredis (redis/set "data" updated-data))))

(defn entered-today [last-date]
  (let [date (java.util.Date. last-date)
        today (java.util.Date.)]
    (and
      (= (.getYear date) (.getYear today))
      (= (.getMonth date) (.getMonth today))
      (= (.getDate date) (.getDate today))
      )))

(defn json-response [data & [status]]
  {:status (or status 200)
   :headers {"Content-Type" "application/json"}
   :body (json/generate-string data)})

(defroutes main-routes
  (GET "/" []
    (let [weights (wredis (redis/get "data"))]
      (index-page weights (or (nil? weights) (not (entered-today (:date (last weights))))))))
  (POST "/save" {params :params session :session}
    (save-weight (:weight params))
    (ring/redirect "/")
    )
  (GET "/redis" [] (wredis (redis/ping)))
  (GET "/clear" []
    (wredis (redis/set "data" nil))
    (ring/redirect "/"))
  (GET "/today" []
    (println "today")
    (let [last-weight-entry (last (wredis (redis/get "data")))]
      (json-response {"weight" (if (and (not (nil? last-weight-entry)) (entered-today (:date last-weight-entry))) (:weight last-weight-entry) nil)}))
    )
  (route/resources "/")
  (route/not-found "Page not found"))

(def app
  (-> (handler/site main-routes)
    (wrap-base-url)))