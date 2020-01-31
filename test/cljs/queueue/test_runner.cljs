(ns queueue.test-runner
  (:require
   [doo.runner :refer-macros [doo-tests]]
   [queueue.core-test]
   [queueue.common-test]))

(enable-console-print!)

(doo-tests 'queueue.core-test
           'queueue.common-test)
