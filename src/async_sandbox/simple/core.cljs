(ns async-sandbox.simple.core)

(defn say [msg] (js/alert msg))

(say "hello")
