(defproject isopen "0.1.0-SNAPSHOT"
  :main "isopen"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"] 
                 [http-kit "2.0.0"]
                 [aleph "0.4.1"]
                 [clj-slack-client "0.1.6-SNAPSHOT"]]
  :profiles {:uberjar {:aot :all}})