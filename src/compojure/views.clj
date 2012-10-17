(ns compojure.views
  (:use [hiccup core page form element]))

(defn index-page [weights]
  (html5
    [:head [:title "Weight Tracker"]
     (include-css "/css/bootstrap-responsive.min.css", "/css/bootstrap.min.css")]
    [:div {:class "container"}
     [:div {:id "holder"} (ordered-list {:style "display: none"} weights)]
     [:div (form-to [:post "/save"]
       (label "weight" "Today's weight")
       (text-field {:maxlength 4 :autofocus true} "weight"))]
     ]
    [:script {:src "https://www.google.com/jsapi"}]
    [:script {:src "//ajax.googleapis.com/ajax/libs/jquery/1.8.2/jquery.min.js"}]
    (include-js "/javascript/highcharts.js", "/javascript/script.js")
    ))