(ns compojure.routes
  (:use compojure.core
        compojure.views
        [hiccup.middleware :only (wrap-base-url)])
  (:require [compojure.route :as route]
            [compojure.handler :as handler]
            [ring.util.response :as ring]
            [compojure.response :as response]
            [taoensso.carmine :as redis]))

(def pool (redis/make-conn-pool))
(def spec-server1 (redis/make-conn-spec :host "127.0.0.1"
                    :port 6379
                    :timeout 4000))

(defmacro wredis [& body] `(redis/with-conn pool spec-server1 ~@body))

(defn save-weight [weight]
  (let [data (wredis (redis/get "data"))
        new-entry {:date (.getTime (java.util.Date.)) :weight weight}
        updated-data (if (nil? data) (vector new-entry) (conj data new-entry))]
    (println "!!!!!!!" updated-data)
    (wredis (redis/set "data" updated-data))))

(defroutes main-routes
  (GET "/" [] (let [weights (wredis (redis/get "data"))] (index-page weights)))
  (POST "/save" {params :params session :session}
    (save-weight (:weight params))
    (ring/redirect "/")
    )
  (GET "/redis" [] (wredis (redis/ping)))
  (GET "/clear" []
    (wredis (redis/set "data" nil))
    (ring/redirect "/"))
  (route/resources "/")
  (route/not-found "Page not found"))

(def app
  (-> (handler/site main-routes)
    (wrap-base-url)))