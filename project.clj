(defproject isopen "0.1.1-SNAPSHOT"
  :main "isopen"
  :description "slack bot"
  :url "https://github.com/eliza0x/is_open"
  :license {:name "MIT LICENCE"
            :url "https://github.com/eliza0x/is_open/blob/master/LICENSE"}
  :dependencies [[org.clojure/clojure "1.8.0"] 
                 [http-kit "2.0.0"]
                 [aleph "0.4.1"]
                 [clj-slack-client "0.1.6-SNAPSHOT"]]
  :profiles {:uberjar {:aot :all}})
