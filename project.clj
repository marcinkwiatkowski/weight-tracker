(defproject compojure "1.0.0-SNAPSHOT"
  :description "FIXME: write description"
  :dependencies [[org.clojure/clojure "1.3.0"]
				 [compojure "1.1.1"]
 	 		     [hiccup "1.0.0"]]
  :plugins [[lein-ring "0.7.1"]]
  :ring {:handler compojure.routes/app})