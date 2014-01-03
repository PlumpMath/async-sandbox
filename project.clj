(defproject async-sandbox "0.1.0-SNAPSHOT"
  :description "a personal core.async playground"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/clojurescript "0.0-2138"]
                 [org.clojure/core.async "0.1.267.0-0d7780-alpha"]

                 ;; [prismatic/dommy "0.1.1"]
                 ]

  :plugins [[lein-cljsbuild "1.0.1"]]

  :cljsbuild {:builds [{:id "simple"
                        :source-paths ["src/async_sandbox/simple"]
                        :compiler {:output-to "resources/gen-js/simple.js"
                                   :optimizations :whitespace
                                   :pretty-print true}}
                       {:id "pubsub"
                        :source-paths ["src/async_sandbox/pubsub"]
                        :compiler {:output-to "resources/gen-js/pubsub.js"
                                   :optimizations :advanced
                                   :pretty-print false}}]})
