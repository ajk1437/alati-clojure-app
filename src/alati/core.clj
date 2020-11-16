(ns alati.core
  (:require [ring.adapter.jetty :as webserver]
            [ring.middleware.reload :refer [wrap-reload]]
            [compojure.core :refer [defroutes GET]]
            [compojure.route :refer [not-found]]
            [ring.handler.dump :refer [handle-dump]]
            [hiccup.core :refer :all]
            [hiccup.page :refer :all]))

(defn welcome
  "A ring handler to respond with a simple welcome message"
  [request]
  (html [:div
         [:h1 "Hello, Clojure World...123"]
         [:p "Welcome to your first Clojure app, I now update automatically"]]
   ))

(defn goodbye
  "A song to wish you goodbye"
  [request]
  (html [:div 
          [:h1 "Walking back to happiness"]
          [:p "Walking back to happiness with you"]
          [:p "Laid aside foolish pride"]
          [:p "Learnt the truth from tears I cried"]]))

(defn about
  "Information about the website developer"
  [request]
  (html [:div 
         [:p "About page"]]))

(defn request-info
  "View the information contained in the request, useful for debugging"
  [request]
  (html (pr-str request)))

(defn hello
  "A simple personalised greeting showing the use of variable path elements"
  [request]
  (let [name (get-in request [:route-params :name])]
    (html (str "Hello " name ". Your name is in URL!"))))

(def operands {"+" + "-" - "*" * ":" /})
(defn calculator
  "A very simple calculator that can add, divide, subtract and multiply.  This is done through the magic of variable path elements."
  [request]
  (let [a  (Integer. (get-in request [:route-params :a]))
        b  (Integer. (get-in request [:route-params :b]))
        op (get-in request [:route-params :op])
        f  (get operands op)]
    (if f
      {:status 200
       :body (str "Calculated result: " (f a b))
       :headers {}}
      {:status 404
       :body "Sorry, unknown operator.  I only recognise + - * : (: is for division)"
       :headers {}})))

(defroutes app
  (GET "/" [] welcome)
  (GET "/goodbye" [] goodbye)
  (GET "/about"   [] about)
  (GET "/request-info" [] handle-dump)
  (GET "/hello/:name" [] hello)
  (GET "/calculator/:op/:a/:b" [] calculator)
  (not-found "Sorry, page not found"))

(defn -main
  "A very simple web server using Ring & Jetty"
  [port-number]
  (webserver/run-jetty app
                       {:port (Integer. port-number)}))
(defn -dev-main
  "A very simple web server using Ring & Jetty that reloads code changes via the development profile of Leiningen"
  [port-number]
  (webserver/run-jetty (wrap-reload #'app)
                       {:port (Integer. port-number)}))