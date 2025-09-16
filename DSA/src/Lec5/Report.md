## Insertion sort
- (1) File input: 
  + 5.0
  + 1.0
  + 3.0
  + 12.0
  + 47.0
  + 177.0
- (2) Random input: 1000 trials with average stable coefficient around 1.5447999999999951E-7.
    Hence, algorithm has running time is `O(n * n)` with sum(end - start) / (n * n) = 1.5447999999999951E-7
- (3) Ascending ordered input: 10000 trials with average coefficient around 1.4686847985194712E-9.
    Hence, algorithm has running time is O(n) with sum(end - start) / n = 1.4686847985194712E-9
  + Coefficient_of_Variation: 49.99249868722294
  + Mean: 4.0E-4
  + Standard_deviation: 0.019996999474889178
  + Coefficient1: 53.76179885373361 (Coefficient of Variation with (end - start) / (n * n))
  + Coefficient2: 50.9888228143628 (Coefficient of Variation with (end - start) / n)
  + Hence, we can ensure that algorithm has running time is `O(n)`
- (4) Descending ordered input:
  + Coefficient_of_Variation: 2.601579514491387
  + Mean: 0.133
  + Standard_deviation: 0.34601007542735446
  + Coefficient1: 2.647935063542427 (Coefficient of Variation with (end - start) / (n * n))
  + Coefficient2: 2.859348871334126 (Coefficient of Variation with (end - start) / n)
  + Hence, we can ensure that algorithm has running time is `O(n * n)`
- (5) Equal-value input:
  + Coefficient_of_Variation: 31.608541725153543
  + Mean: 0.001
  + Standard_deviation: 0.031608541725153545
  + Coefficient1: 33.403908852923756 (Coefficient of Variation with (end - start) / (n * n))
  + Coefficient2: 32.03320477769753 (Coefficient of Variation with (end - start) / n)
  + Hence, we can ensure that algorithm has running time is `O(n)`

## Selection Sort
- (1) File input:
  + 5.0
  + 3.0 
  + 10.0 
  + 19.0 
  + 72.0 
  + 198.0
- (2) Random input: 1000 trials with average stable coefficient around 9.166999999999963E-8.
  Hence, algorithm has running time is `O(n * n)` with sum(end - start) / (n * n) = 9.166999999999963E-8
- (3) Ascending ordered input:
  + Coefficient_of_Variation: 2.087523723490395
  + Mean: 0.2128
  + Standard_deviation: 0.4442250483587561 
  + Coefficient1: 8.214992051719423E-7 (Variation with (end - start) / (n * n))
  + Coefficient2: 5.788895880940655E-4 (Variation with (end - start) / n)
  + Hence, we can ensure that algorithm has running time is `O(n * n)`
- (4) Descending ordered input:
  + Coefficient_of_Variation: 1.326012423357943 
  + Mean: 0.4259 
  + Standard_deviation: 0.5647486911081478 
  + Coefficient1: 1.0870013005087208E-6 (Variation with (end - start) / (n * n))
  + Coefficient2: 7.474772832466701E-4 (Variation with (end - start) / n)
  + Hence, we can ensure that algorithm has running time is `O(n * n)`
- (5) Equal-value input:
  + Coefficient_of_Variation: 0.14956650117921155 
  + Mean: 43.0287 
  + Standard_deviation: 6.435652109289941 
  + Coefficient1: 6.435652109289653E-8 (Variation with (end - start) / (n * n))
  + Coefficient2: 6.435652109289779E-4 (Variation with (end - start) / n)
  + Hence, we can ensure that algorithm has running time is `O(n * n)`
## Some evaluation:
- When evaluating time, there are many effects on running time. So, value just is a reference to running
time to `mathematical analysis`.
- Selection sorted array is insensitive with input
- Meanwhile, Insertion sort is sensitive with input. With ascending sorted array, or partial sorted array,
Insertion has running time is linear `O(n)`
- 
## Answer for Exercise 8
- In these sort algorithms, we shouldn't save input values in LinkedList because they require to swap
these values while sorting.

## MergeSort:
- (1) File input:
  + 2.0
  + 1.0
  + 1.0
  + 2.0
  + 3.0
  + 7.0
- (2) Random input: 1000 trials with average stable coefficient around 5.867499999999994E-9
  Hence, algorithm has running time is `O(nlogn)` with sum(end - start) / nlog(n) = 5.867499999999994E-9
- (3) Ascending ordered input: 10000 trials with average coefficient around 1.4686847985194712E-9.
  Hence, algorithm has running time is O(n) with sum(end - start) / nlog(n) = 1.4686847985194712E-9
  + Coefficient_of_Variation: 9.580324407950426
  + Mean: 0.0128
  + Standard_deviation: 0.12262815242176546
  + Coefficient1: 2.58061101762573E-5 (Coefficient of Variation with (end - start) / (n * log(n)) )
  + Coefficient2: 1.6878038037828434E-4 (Coefficient of Variation with (end - start) / n)
  + Hence, we can ensure that algorithm has running time is `O(nlogn)`
- (4) Descending ordered input:
  + Coefficient_of_Variation: 7.815459460555022
  + Mean: 0.0165
  + Standard_deviation: 0.12895508109915788
  + Coefficient1: 8.005155491298222 (Coefficient of Variation with (end - start) / (n * log(n)) )
  + Coefficient2: 8.014136189269037 (Coefficient of Variation with (end - start) / n)
  + Hence, we can ensure that algorithm has running time is `O(nlogn)`
- (5) Equal-value input:
  + Coefficient_of_Variation: 9.670951925388952
  + Mean: 0.0115
  + Standard_deviation: 0.11121594714197294
  + Coefficient1: 9.142817484014654 (Coefficient of Variation with (end - start) / (n * log(n)) )
  + Coefficient2: 9.791598178338221 (Coefficient of Variation with (end - start) / n)
  + Hence, we can ensure that algorithm has running time is `O(nlogn)`

- Merge sort is insensitive with input
- The essence of merge sort is `merge` which has running time is `O(nlog(n))`, can be `O(1/2O(nlog(n)))` 
with practical improvement `Stop if already sorted`

## QuickSort

- (1) File input:
  + 1.0 
  + 2.0 
  + 1.0 
  + 1.0 
  + 3.0
- (2) Random input: 1000 trials with average stable coefficient around 9.208877126991989E-6
  Hence, algorithm has running time is `O(nlogn)` with sum(end - start) / nlog(n) = 9.208877126991989E-6
- (3) Ascending ordered input: 10000 trials with average coefficient around 1.4686847985194712E-9.
  Hence, algorithm has running time is O(n) with sum(end - start) / nlog(n) = 1.4686847985194712E-9
  + Coefficient_of_Variation: 4.616672985110628
  + Mean: 0.0505
  + Standard_deviation: 0.23314198574808673
  + Coefficient1: 4.821547226189461E-5 (Coefficient of Variation with (end - start) / (n * log(n)) )
  + Coefficient2: 1.6878038037828434E-4 (Coefficient of Variation with (end - start) / n)
  + Hence, we can ensure that algorithm has running time is `O(nlogn)`
- (4) Descending ordered input:
  + Coefficient_of_Variation: 4.77762991140492
  + Mean: 0.0473
  + Standard_deviation: 0.2259818948094527
  + Coefficient1: 4.92060688425213 (Coefficient of Variation with (end - start) / (n * log(n)) )
  + Coefficient2: 4.93656867656068 (Coefficient of Variation with (end - start) / n)
  + Hence, we can ensure that algorithm has running time is `O(nlogn)`
- (5) Equal-value input:
  + Coefficient_of_Variation: 1.707515055921833
  + Mean: 0.3236
  + Standard_deviation: 0.5525518720963052
  + Coefficient1: 1.736277405876345 (Coefficient of Variation with (end - start) / (n * log(n)) )
  + Coefficient2: 1.7448674386989174 (Coefficient of Variation with (end - start) / n)
  + Hence, we can ensure that algorithm has running time is `O(nlogn)`

- QuickSort have time complexity with compare model is O(1.39nlog(n)) (possibility guarantee) in average 
O(1/2*n^2) in worst case and O(nlog(n)) in best case 