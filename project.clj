(defproject alati "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [ring "1.8.2"]
                 [compojure "1.6.2"]
                 [hiccup "1.0.5"]]
  
  
  :repl-options {:init-ns alati.core}
  :main alati.core
  
  :profiles {:dev
             {:main alati.core/-dev-main}}
  )