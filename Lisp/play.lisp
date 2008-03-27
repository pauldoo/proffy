(defun has-number-p (x)
    ;(format t "  DEBUG: ~A~%" x)
    (if (listp x)
        (and
            (not (null x))
            (or (has-number-p (car x)) (has-number-p (cdr x))))
        (numberp x)))

(defun test (x)
    (format t "In: ~A~%" x)
    (format t "Out: ~A~%" (has-number-p x)))

(test 1)
(test 'a)
(test '(a (b (c d) ((3)))))

