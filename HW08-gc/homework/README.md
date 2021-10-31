### HW08-gc

Mac (Retina 5K, 27-inch, Late 2015)
3,2 GHz 4‑ядерный процессор IntelCorei5
16 ГБ 1867 MHz DDR3

Расход heap
-Xms800m
-Xmx800m
 14sec как если бы без указания heap-а

> После приведения Integer-полей  
 
- Summator к примитивам -> до 4sec
- Data к примитивам -> до 2sec
  
Расход heap - можем сократить до
-Xms450m
-Xmx450m

Resume: наглядно и убедительно 