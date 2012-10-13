(ns compojure.routes
  (:use compojure.core
        compojure.views
        [hiccup.middleware :only (wrap-base-url)])
  (:require [compojure.route :as route]
            [compojure.handler :as handler]
            [ring.util.response :as ring]
            [compojure.response :as response]))

(defroutes main-routes
  (GET "/" {session :session} (let [weights (session :weights )] (index-page weights)))
  (POST "/save" {params :params session :session}
    (let [new-weight (:weight params)
          weights (:weights session)
          session (assoc session :weights (if (nil? weights) (vector new-weight) (conj weights new-weight)))]
      (-> (ring/redirect "/")
        (assoc :session session))
      )
    )
  (route/resources "/")
  (route/not-found "Page not found"))

(def app
  (-> (handler/site main-routes)
    (wrap-base-url)))