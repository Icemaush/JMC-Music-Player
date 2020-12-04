package jmcmusicplayer;

/**
 * Name: Reece Pieri
 * ID: M087496
 * Date: 30/11/2020
 * Course: Java III
 * Assessment: AT3 Project
 */
public class Sort {
    
    public Song[] MergeSort(Song[] array) {
        if (array.length <= 1) {
            return array;
        }
        
        Song[] first = new Song[array.length / 2];
        Song[] second = new Song[array.length - first.length];
        System.arraycopy(array, 0, first, 0, first.length);
        System.arraycopy(array, first.length, second, 0, second.length);
        
        MergeSort(first);
        MergeSort(second);
        
        
        return Merge(first, second, array);
    }
    
    private Song[] Merge(Song[] first, Song[] second, Song[] result) {
        int firstIndex = 0;
        int secondIndex = 0;
        int mergedIndex = 0;

        while (firstIndex < first.length && secondIndex < second.length) {
            if (first[firstIndex].getName().compareTo(second[secondIndex].getName()) < 0) {
                result[mergedIndex] = first[firstIndex];
                firstIndex++;
            } else {
                result[mergedIndex] = second[secondIndex];
                secondIndex++;
            }
            mergedIndex++;
        }
        
        System.arraycopy(first, firstIndex, result, mergedIndex, first.length - firstIndex);
        System.arraycopy(second, secondIndex, result, mergedIndex, second.length - secondIndex);
        
        return result;
    }
}
