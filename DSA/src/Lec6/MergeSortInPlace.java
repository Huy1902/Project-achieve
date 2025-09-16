package Lec6;

public class MergeSortInPlace {
  static void merge(int arr[], int start, int mid,
                    int end)
  {
    int start2 = mid + 1;

    // If the direct merge is already sorted
    if (arr[mid] <= arr[start2]) {
      return;
    }

    // Sadly it is O(N^2) for merge so I don't complete it yet
    while (start <= mid && start2 <= end) {

      // If element 1 is in right place
      if (arr[start] <= arr[start2]) {
        start++;
      }
      else {
        int value = arr[start2];
        int index = start2;

        // Shift all the elements between element 1
        // element 2, right by 1.
        while (index != start) {
          arr[index] = arr[index - 1];
          index--;
        }
        arr[start] = value;

        // Update all the pointers
        start++;
        mid++;
        start2++;
      }
    }
  }

  static void mergeSort(int arr[], int l, int r)
  {
    if (l < r) {
      int m = l + (r - l) / 2;

      mergeSort(arr, l, m);
      mergeSort(arr, m + 1, r);

      merge(arr, l, m, r);
    }
  }
}
