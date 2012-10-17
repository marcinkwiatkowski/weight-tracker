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

(defroutes main-routes
  (GET "/" [] (let [weights (wredis (redis/get "weights"))] (index-page weights)))
  (POST "/save" {params :params session :session}
    (let [new-weight (:weight params)
          weights (wredis (redis/get "weights"))
          new-weights (if (nil? weights) (vector new-weight) (conj weights new-weight))]
      (wredis (redis/set "weights" new-weights))
      (ring/redirect "/"))
    )
  (GET "/redis" [] (wredis (redis/ping)))
  (GET "/clear" [] (wredis (redis/set "weights" nil)))
  (route/resources "/")
  (route/not-found "Page not found"))

(def app
  (-> (handler/site main-routes)
    (wrap-base-url)))