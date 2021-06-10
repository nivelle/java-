//给定一个按照升序排列的整数数组 nums，和一个目标值 target。找出给定目标值在数组中的开始位置和结束位置。 
//
// 如果数组中不存在目标值 target，返回 [-1, -1]。 
//
// 进阶： 
//
// 
// 你可以设计并实现时间复杂度为 O(log n) 的算法解决此问题吗？ 
// 
//
// 
//
// 示例 1： 
//
// 
//输入：nums = [5,7,7,8,8,10], target = 8
//输出：[3,4] 
//
// 示例 2： 
//
// 
//输入：nums = [5,7,7,8,8,10], target = 6
//输出：[-1,-1] 
//
// 示例 3： 
//
// 
//输入：nums = [], target = 0
//输出：[-1,-1] 
//
// 
//
// 提示： 
//
// 
// 0 <= nums.length <= 105 
// -109 <= nums[i] <= 109 
// nums 是一个非递减数组 
// -109 <= target <= 109 
// 
// Related Topics 数组 二分查找 
// 👍 1048 👎 0


//leetcode submit region begin(Prohibit modification and deletion)
class Solution {
    public int[] searchRange(int[] nums, int target) {
        //二分法查找左边边界值
        int leftIdx = binarySearch(nums, target, true);
        //二分法查找右边边界值
        int rightIdx = binarySearch(nums, target, false) - 1;
        if (leftIdx <= rightIdx && rightIdx < nums.length && nums[leftIdx] == target && nums[rightIdx] == target) {
            return new int[]{leftIdx, rightIdx};
        }
        return new int[]{-1, -1};
    }

    public int binarySearch(int[] nums, int target, boolean lower) {
        int left = 0;
        int right = nums.length - 1;
        //元素个数
        int ans = nums.length;
        //退出条件: 指针交汇
        while (left <= right) {
            //中间位移
            int mid = (left + right) / 2;
            //左班部分 考虑 中位移值是否大于等于 目标值
            //右半部分 只需要考虑中位移只是否大于目标值
            if (nums[mid] > target || (lower && nums[mid] >= target)) {
                //右下标左移
                right = mid - 1;
                //子集元素个数
                ans = mid;
            } else {
                //左下标右移
                left = mid + 1;
            }
        }
        return ans;
    }
}

//leetcode submit region end(Prohibit modification and deletion)
