package com.zj.lotteryAnalyze.service;

import com.zj.lotteryAnalyze.aliyunApi.YiYuanHistory;
import com.zj.lotteryAnalyze.dto.LotteryInfo;
import com.zj.lotteryAnalyze.utils.MyUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/1/26.
 */
public class LotteryStatOfLastTwo {

	private YiYuanHistory yiYuanHistory = new YiYuanHistory();

	/**
	 * 统计每期中最后两位数是大数的次数
	 * @param lotteryInfos
	 * @return
	 */
	public int[][] countBigNumOfLastTwo(List<LotteryInfo> lotteryInfos){

		List<int[]> list = getLastTwoNumber(lotteryInfos);
		int[][] bigNumTimes = new int[1][2];
		int tenBig = 0;
		int unitBig = 0;
		for(int[] group: list){
			if(group[0]>= 5){
				tenBig++;
			}
			if(group[1]>= 5){
				unitBig++;
			}
		}
		bigNumTimes[0][0] = tenBig;
		bigNumTimes[0][1] = unitBig;

		return bigNumTimes;
	}

	/**
	 * 统计以五进制表示的最后两位数，在五进制的个位上
	 * 出现奇数的次数
	 */
	public int[] countOddNumQuinaryOfLastTwo(List<LotteryInfo> lotteryInfos){

		int[][][] lotterys = getLastTwoQuinary(lotteryInfos);
		int[] oddTimes = new int[2];
		int tenOddTimes = 0;
		int unitOddTimes = 0;

		for(int i=0; i<6; i++){
			for(int j=0; j<2; j++){
				//j==0表示每期彩票中的倒数第二个开奖号码
				if(j == 0){
					if(lotterys[i][j][1]%2 == 0){
						tenOddTimes++;
					}
				}
				//j==1表示每期彩票中的最后一个开奖号码
				if(j == 1){
					if(lotterys[i][j][1]%2 == 0){
						unitOddTimes++;
					}
				}
			}
		}
		oddTimes[0] = tenOddTimes;
		oddTimes[1] = unitOddTimes;

		return oddTimes;
	}

	/**
	 * 统计以五进制表示的最后两位数，个位数相同的次数
	 * 例如8和3以五进制表示个位上都是3
	 * @param lotteryInfos
	 * @return
	 */
	public int[] countSameNumQuinaryLast(List<LotteryInfo> lotteryInfos){



		return null;
	}

	/**
	 * 统计以五进制表示的最后两位数，不同的个位数的个数
	 * 比如，0,5,8,3,7 个位上不同的位数有3位
	 * 其中0和5,8和3以五进制表示个位数是相同的
	 * @param lotteryInfos
	 * @return
	 */
	public int[][] countDiffQuinary(List<LotteryInfo> lotteryInfos){

		return null;
	}

	/**
	 * 统计以五进制表示的最后两位数，个位连续相同的最大次数
	 * 比如，4,9,3,8,3那么最大连续次数为3,8,3以五进制表示个位是相同的
	 * @param lotteryInfos
	 * @return
	 */
	public int[] countSameContinueQuinary(List<LotteryInfo> lotteryInfos){

		int[][][] lotterys = getLastTwoQuinary(lotteryInfos);
		int[] conTimes = new int[2];
		int tenMaxCon = 0;
		int unitMaxCon = 0;
		int tenTemMaxCon = 0;
		int unitTemMaxCon = 0;

		for(int i=1; i<6; i++){
			for(int j=0; j<2; j++){
				if(j == 0){
					if(lotterys[i][j][1] == lotterys[i-1][j][1]){
						tenTemMaxCon++;
						if(tenTemMaxCon > tenMaxCon){
							tenMaxCon = tenTemMaxCon;
						}
					}else{
						tenTemMaxCon = 0;
					}
				}
				if(j == 1){
					if(lotterys[i][j][1] == lotterys[i-1][j][1]){
						unitMaxCon++;
						if(unitTemMaxCon > unitMaxCon){
							unitMaxCon = unitTemMaxCon;
						}
					}else{
						unitTemMaxCon = 0;
					}
				}
			}
		}
		conTimes[0] = tenMaxCon;
		conTimes[1] = unitMaxCon;

		return conTimes;
	}

	/**
	 * 以五进制表示的最后两位数，个位上的和计算
	 * 例如0,0,3,6,9个位上的和为0+0+3+1+4=8
	 * @param lotteryInfos
	 * @return
	 */
	public int[][] lastSumQuinary(List<LotteryInfo> lotteryInfos){
		return null;
	}


	/**
	 * 获取数据源
	 * 12天数据，每天50期，共600期数据
	 * @return
	 */
	public List<LotteryInfo> getHistoryData(){
		List<List<LotteryInfo>> list = yiYuanHistory.getLotteryHisOfDays(12);
		List<LotteryInfo> lotteryInfos = new ArrayList<>();

		for(List<LotteryInfo> lotteryInfoList: list){
			for(LotteryInfo info: lotteryInfoList){
				lotteryInfoList.add(info);
			}
		}

		return lotteryInfos;
	}

	/**
	 * 对数据源进行分组，每6期数据为一组
	 * @param list
	 * @return
	 */
	public List<List<LotteryInfo>> groupLotterys(List<LotteryInfo> list){

		List<List<LotteryInfo>> lotteryGroups = new ArrayList<>();

		for(int i=0; i<list.size(); i++){
			List<LotteryInfo> lotteryInfos = null;
			if(i%6 == 0){
				lotteryInfos = new ArrayList<>();
				lotteryInfos.add(list.get(i));
			}else{
				lotteryInfos.add(list.get(i));
				if(i%6 == 5){
					lotteryGroups.add(lotteryInfos);
				}
			}
		}
		return lotteryGroups;
	}

	/**
	 * 获取每组中每期的最后两个数字
	 * @return
	 */
	public List<int[]> getLastTwoNumber(List<LotteryInfo> lotteryInfos){

		List<int[]> list = new ArrayList<>();

		for(LotteryInfo info: lotteryInfos){
			int[] lastTwo = new int[2];
			int[] perNum = info.convertToPerNum();
			lastTwo[0] = perNum[3];
			lastTwo[1] = perNum[4];
			list.add(lastTwo);
		}

		return list;
	}

	/**
	 * 获得每组中每期最后两位数字的五进制形式
	 * @param lotteryInfos
	 * @return
	 */
	public int[][][] getLastTwoQuinary(List<LotteryInfo> lotteryInfos){

		MyUtil util = new MyUtil();

		List<int[]> list = getLastTwoNumber(lotteryInfos);
		int[][][] lastTwoQuinary = new int[6][2][2];

		for(int i=0; i<list.size(); i++){
			for(int j=0; j<list.get(i).length; j++){
				lastTwoQuinary[i][j] = util.conventNumToQuinary(list.get(i)[j]);
			}
		}

		return lastTwoQuinary;
	}

}