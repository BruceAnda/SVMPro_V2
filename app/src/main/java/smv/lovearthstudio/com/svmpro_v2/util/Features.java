package smv.lovearthstudio.com.svmpro_v2.util;

public class Features{

	/**求数组最小值 double**/
	public static double minimum(double data[]){
		if(data == null || data.length == 0) return 0.0;
		int length = data.length;
		double MIN = data[0];
		for (int i = 1; i < length; i++){
			MIN = data[i]<MIN?data[i]:MIN;
		}
		return MIN;
	}
	/**求数组最小值 float**/
	public static double minimum(float data[]){
		if(data == null || data.length == 0) return 0.0;
		int length = data.length;
		float MIN = data[0];
		for (int i = 1; i < length; i++){
			MIN = data[i]<MIN?data[i]:MIN;
		}
		return MIN;
	}
	
	/**求数组最小值 int**/
	public static double minimum(int data[]){
		if(data == null || data.length == 0) return 0.0;
		int length = data.length;
		double MIN = data[0];
		for (int i = 1; i < length; i++){
			MIN = data[i]<MIN?data[i]:MIN;
		}
		return MIN;
	}

	/**求数组最大值 double**/
	public static double maximum(double data[]){
		if(data == null || data.length == 0) return 0.0;
		
		int length = data.length;
		double Max = data[0];
		for (int i = 1; i<length; i++)
			Max = data[i]<Max ? Max : data[i];
		return Max;
	}
	
	/**求数组最大值 float**/
	public static double maximum(float data[]){
		if(data == null || data.length == 0) return 0.0;		
		int length = data.length;
		float Max = data[0];
		for (int i = 1; i<length; i++)
			Max = data[i]<Max ? Max : data[i];
		return Max;
	}
	
	/**求数组最大值 int **/
	public static double maximum(int data[]){
		if(data == null || data.length == 0) return 0.0;	
		int length = data.length;
		int Max = data[0];
		for (int i = 1; i<length; i++)
			Max = data[i]<Max ? Max : data[i];
		return Max;
	}

	/**求数组的方差**/
	public static double variance(double data[]){
		if(data == null || data.length == 0) return 0.0;	
		int length = data.length;
		double average = 0, s = 0, sum = 0;
		for (int i = 0; i<length; i++)
		{
			sum = sum + data[i];
		}
		average = sum / length;
		for (int i = 0; i<length; i++)
		{
			s = s + Math.pow(data[i] - average, 2);
		}
		s = s / length;
		return s;
	}

	/**求数组的方差 float**/
	public static double variance(float data[]){
		if(data == null || data.length == 0) return 0.0;	
		int length = data.length;
		double average = 0, s = 0, sum = 0;
		for (int i = 0; i<length; i++)
		{
			sum = sum + data[i];
		}
		average = sum / length;
		for (int i = 0; i<length; i++)
		{
			s = s + Math.pow(data[i] - average, 2);
		}
		s = s / length;
		return s;
	}

	/**求数组的方差 int **/
	public static double variance(int data[]){
		if(data == null || data.length == 0) return 0.0;	
		int length = data.length;
		double average = 0, s = 0, sum = 0;
		for (int i = 0; i<length; i++)
		{
			sum = sum + data[i];
		}
		average = sum / length;
		for (int i = 0; i<length; i++)
		{
			s = s + Math.pow(data[i] - average, 2);
		}
		s = s / length; 
		return s;
	}

	/**求double数组过均值率**/
	public static double meanCrossingsRate(double data[]){
		if(data == null || data.length == 0) return 0.0;	
		int length = data.length;
		double Sum = 0;
		double num = 0;
		double[] copydata = new double[length];
		for (int i = 0; i < length; i++)
		{
			copydata[i] = data[i];
			Sum +=copydata[i];
		}
		double avg = Sum/length;
		for (int i = 0; i < length; i++)
		{
			copydata[i] = copydata[i] - avg;
		}
		for (int i = 0; i < length - 1; i++)
		{
			if (copydata[i] * copydata[i + 1]< 0){
				num++;
			}
		}
		return num / length;
	}
	
	/**求float数组过均值率**/
	public static double meanCrossingsRate(float data[]){
		if(data == null || data.length == 0) return 0.0;	
		int length = data.length;
		float Sum = 0;
		float num = 0;
		float[] copydata = new float[length];
		for (int i = 0; i < length; i++)
		{
			copydata[i] = data[i];
			Sum +=copydata[i];
		}
		float avg = Sum/length;
		for (int i = 0; i < length; i++)
		{
			copydata[i] = copydata[i] - avg;
		}
		for (int i = 0; i < length - 1; i++)
		{
			if (copydata[i] * copydata[i + 1]< 0){
				num++;
			}
		}
		return num / length;
	}


	
	/**求double数组的标准差**/
	public static double standardDeviation(double data[]){
		if(data == null || data.length == 0) return 0.0;	
		double s = variance(data);
		s = Math.sqrt(s); 
		return s;
	}

	/**求float数组的标准差 float**/
	public static double standardDeviation(float data[]){
		if(data == null || data.length == 0) return 0.0;	
		double s = variance(data);
		s = Math.sqrt(s); 
		return s;
	}


	/**
	 * 求一个double数组中的最大值所在的位置=谱峰位置
	 * @param data FFT array
	 * @return
	 */
	public static double spp(double[] data){
		if(data == null || data.length == 0) return 0;	
		int r = 0;
		double max = data[0];
		for (int i = 0; i < data.length; i++) {
			if (data[i] > max ) {
				r = i;
				max = data[i];
			}
		}
		return r;
	}
	
	
	/**
	 * 频域 能量
	 * FIXME:如果data数组有误，应该返回什么？
	 * @param data FFT array
	 */
	public static double energy(double[] data){
		if(data == null || data.length == 0) return 0.0;	
		double sum = 0;
		for(int i=0;i<data.length;i++){
			sum+=Math.pow(data[i],2);
		}
		return sum/data.length;
	}
	
	/**
	 * 热力学函数  熵
	 * java.lang.Math.log(double a) 返回自然对数（以e为底）的一个double值。特殊情况：如果参数是NaN或小于零，那么结果是NaN.如果参数是正无穷大，那么结果是正无穷大。如果参数是正零或负零，那么结果是负无穷大。
	 * @param data  有loge=ln运算不允许有和零值
	 * @return
	 * @throws InfoException 
	 */
	public static double entropy(double[] data){
		if(data == null || data.length == 0) return 0.0;
		double temp;
		double sum = 0;
		for(int i=0;i<data.length;i++){
			temp = data[i];
			if(temp > 0){
				sum+=Math.log(temp)*temp;
			}
		}
		return sum*-1;
	}

	/**求数组的平均值 double**/
	public static double mean(double data[]){
		if(data == null || data.length == 0) return 0.0;
		int length = data.length;
		double Sum = 0;
		for (int i = 0; i < length; i++)
			Sum = Sum + data[i];
		return Sum / length;
	}
	
	/**求数组的平均值 float**/
	public static double mean(float data[]){
		if(data == null || data.length == 0) return 0.0;
		int length = data.length;
		float Sum = 0;
		for (int i = 0; i < length; i++)
			Sum = Sum + data[i];
		return Sum / length;
	}

	/**
	 * 质心 centroid : SUM(i*f(i)*f(i))/SUM(f(i)*f(i))
	 * 见Intelligent Sleep Stage Mining Service with 
	 * Smartphones 
	 * @param data
	 * @return
	 */
	public static double centroid(double[] data){
		if(data == null || data.length == 0) return 0.0;
		double sum1 = 0;
		double sum2 = 0;
		double temp;
		double tempPow;
		for(int i=0;i<data.length;i++){
			temp = data[i];
			tempPow = temp*temp;
			sum1+=tempPow;
			sum2+=tempPow*i;
		}
		return sum2/sum1;
	}
	
	/**求信号向量幅值 （Signal vector magnitude）**/
	public static double signalVectorMagnitude(double data[]){
		int length = data.length;
		double MaxMagnitude = 0;
		for (int i = 0; i < length; i++){
			if (data[i] < 0)
				MaxMagnitude = (-data[i])<MaxMagnitude ? MaxMagnitude : (-data[i]);
			else
				MaxMagnitude = data[i]<MaxMagnitude ? MaxMagnitude : data[i];
		}
		return MaxMagnitude;
	}
	
	
	/**求信号向量幅值 （Signal vector magnitude）**/
	public static double signalVectorMagnitude(float data[]){
		int length = data.length;
		double MaxMagnitude = 0;
		for (int i = 0; i < length; i++){
			if (data[i] < 0)
				MaxMagnitude = (-data[i])<MaxMagnitude ? MaxMagnitude : (-data[i]);
			else
				MaxMagnitude = data[i]<MaxMagnitude ? MaxMagnitude : data[i];
		}
		return MaxMagnitude;
	}
	/**求信号向量幅值 （Signal vector magnitude）**/
	public static double signalVectorMagnitude(int data[]){
		int length = data.length;
		double MaxMagnitude = 0;
		for (int i = 0; i < length; i++){
			if (data[i] < 0)
				MaxMagnitude = (-data[i])<MaxMagnitude ? MaxMagnitude : (-data[i]);
			else
				MaxMagnitude = data[i]<MaxMagnitude ? MaxMagnitude : data[i];
		}
		return MaxMagnitude;
	}
	
	
	/**
	 * 四分位数（Quartile） 1/4
	 * FIXME: 这里使用了一个低效的排序算法,需要改进。
	 * **/
	public static double firstQuartile(double data[]){
		if(data == null || data.length == 0) return 0.0;
		int length = data.length;
		double[] copydata = new double[length];
		for (int i = 0; i < length; i++){
			copydata[i] = data[i];
		}
		for (int i = 0; i<length; i++){
			for (int j = i + 1; j<length; j++)
			{
				if (copydata[j]<copydata[i])
				{
					double t;
					t = copydata[i];
					copydata[i] = copydata[j];
					copydata[j] = t;
				}
			}
		}
		double q = 1 + (length - 1) *0.25;
		int b = (int)q;
		double d = q - b;
		double quartile1 = copydata[b-1] + (copydata[b] - copydata[b-1])*d;
		return quartile1;
	}
	
	/**
	 * 
	 * 四分位数（Quartile） 1/4
	 * 
	 * FIXME: 这里使用了一个低效的排序算法,需要改进。
	 * **/
	public static double firstQuartile(float data[]){
		if(data == null || data.length == 0) return 0.0;
		int length = data.length;
		double[] copydata = new double[length];
		for (int i = 0; i < length; i++){
			copydata[i] = data[i];
		}
		for (int i = 0; i<length; i++){
			for (int j = i + 1; j<length; j++)
			{
				if (copydata[j]<copydata[i])
				{
					double t;
					t = copydata[i];
					copydata[i] = copydata[j];
					copydata[j] = t;
				}
			}
		}
		double q = 1 + (length - 1) *0.25;
		int b = (int)q;
		double d = q - b;
		double quartile1 = copydata[b-1] + (copydata[b] - copydata[b-1])*d;
		return quartile1;
	}

	/**四分位数（Quartile） 3/4**/
	public static double thirdQuartile(double data[]){
		int length = data.length;
		double[] copydata = new double[length];
		for (int i = 0; i < length; i++)
		{
			copydata[i] = data[i];
		}
		for (int i = 0; i<length; i++)
		{
			for (int j = i + 1; j<length; j++)
			{
				if (copydata[j]<copydata[i])
				{
					double t;
					t = copydata[i];
					copydata[i] = copydata[j];
					copydata[j] = t;
				}
			}
		}
		double q = 1 + (length - 1) *0.75;
		int b = (int)q;
		double d = q - b;
		double quartile3 = copydata[b-1] + (copydata[b] - copydata[b-1])*d;
		return quartile3;
	}
	
	/**四分位数（Quartile） 3/4**/
	public static double thirdQuartile(float data[]){
		int length = data.length;
		double[] copydata = new double[length];
		for (int i = 0; i < length; i++)
		{
			copydata[i] = data[i];
		}
		for (int i = 0; i<length; i++)
		for (int j = i + 1; j<length; j++)
		{
			if (copydata[j]<copydata[i])
			{
					double t;
					t = copydata[i];
					copydata[i] = copydata[j];
					copydata[j] = t;
			}
		}

		double q = 1 + (length - 1) *0.75;
		int b = (int)q;
		double d = q - b;
		double quartile3 = copydata[b-1] + (copydata[b] - copydata[b-1])*d;
		return quartile3;
	}
	/**四分位数（Quartile） 3/4**/
	public static double thirdQuartile(int data[]){
		int length = data.length;
		double[] copydata = new double[length];
		for (int i = 0; i < length; i++)
		{
			copydata[i] = data[i];
		}
		for (int i = 0; i<length; i++)
		{
			for (int j = i + 1; j<length; j++)
			{
				if (copydata[j]<copydata[i])
				{
					double t;
					t = copydata[i];
					copydata[i] = copydata[j];
					copydata[j] = t;
				}
			}
		}
		double q = 1 + (length - 1) *0.75;
		int b = (int)q;
		double d = q - b;
		double quartile3 = copydata[b-1] + (copydata[b] - copydata[b-1])*d;
		return quartile3;
	}
	/**求数组过零率**/
	public static double zeroCrossingRate(double data[]){
		int length = data.length;
		double num = 0;
		for (int i = 0; i < length - 1; i++)
		{
			if (data[i] * data[i + 1]< 0){
				num++;
			}
		}
		return num / length;
	}
	/**求数组过零率**/
	public static double zeroCrossingRate(float data[]){
		int length = data.length;
		double num = 0;
		for (int i = 0; i < length - 1; i++)
		{
			if (data[i] * data[i + 1]< 0){
				num++;
			}
		}
		return num / length;
	}

	/**相关系数*/
	public static double correlation(double data1[], double data2[]){
		int length1 = data1.length;
		int length2 = data2.length;
		double mean1 = mean(data1);
		double mean2 = mean(data2);
		double covariance = 0.0;
		for (int i = 0; i < length1; i++)
		{
			covariance += (data1[i] - mean1)*(data2[i] - mean2);
		}
		double standarddeviation1 = standardDeviation(data1);
		double standarddeviation2 = standardDeviation(data2);
		return (covariance / length1) / (standarddeviation1*standarddeviation2);
	}
	/**相关系数*/
	public static double correlation(float data1[], float data2[]){
		int length1 = data1.length;
		int length2 = data2.length;
		double mean1 = mean(data1);
		double mean2 = mean(data2);
		double covariance = 0.0;
		for (int i = 0; i < length1; i++)
		{
			covariance += (data1[i] - mean1)*(data2[i] - mean2);
		}
		double standarddeviation1 = standardDeviation(data1);
		double standarddeviation2 = standardDeviation(data2);
		return (covariance / length1) / (standarddeviation1*standarddeviation2);
	}


	/**中位数（Median）**/  
	public static double median(double data[]){
		int length = data.length;
		double[] copydata = new double[length];
		for (int i = 0; i < length; i++){
			copydata[i] = data[i];
		}
		double medium;
		for (int i = 0; i<length; i++){
			for (int j = i + 1; j<length; j++){
				if (copydata[j]<copydata[i]){
					double t;
					t = copydata[i];
					copydata[i] = copydata[j];
					copydata[j] = t;
				}
			}
		}
		if (length % 2 == 1)
			medium = copydata[length / 2];
		else
			medium = (copydata[length / 2 - 1] + copydata[length / 2]) / 2;
		return medium;
	}
	/**中位数（Median）**/  
	public static double median(float data[]){
		int length = data.length;
		double[] copydata = new double[length];
		for (int i = 0; i < length; i++)
		{
			copydata[i] = data[i];
		}
		double medium;
		for (int i = 0; i<length; i++)
		{
			for (int j = i + 1; j<length; j++)
			{
				if (copydata[j]<copydata[i])
				{
					double t;
					t = copydata[i];
					copydata[i] = copydata[j];
					copydata[j] = t;
				}
			}
		}
		if (length % 2 == 1)
			medium = copydata[length / 2];
		else
			medium = (copydata[length / 2 - 1] + copydata[length / 2]) / 2;
		return medium;
	}

	/**特征规范化最值法
	 * 同一列的每个特征除以该列特征中的最大值将特征范围变为（-1,1）
	 * */
	public static double[][] minmax(double[][] data){
		int r = data.length;
		int c = data[0].length;
		double[][] temp =new double[r][c];
		for (int i = 0; i<r; i++)
		{
			temp[i] = new double[c];
		}
		//求每一列的最大值和最小值
		double[][] middata = new double[r][c];
		middata[0] = new double[c];
		middata[1] = new double[c];
		for (int i = 0; i<c; i++)
		{
			double midmin = data[0][i];
			double midmax = data[0][i];
			for (int j = 0; j<r; j++)
			{
				if (midmin>data[j][i])
				{
					midmin = data[j][i];
				}
				if (midmax<data[j][i])
				{
					midmax = data[j][i];
				}
			}
			middata[0][i] = midmin;
			middata[1][i] = midmax;
		}
		//最值法处理数据，存储在数组temp中返回
		for (int i = 0; i<c; i++)
		{
			double a = middata[1][i] - middata[0][i];
			for (int j = 0; j<r; j++)
			{
				double b = data[j][i] - middata[0][i];
				temp[j][i] = b / a;
			}
		}
		return temp;
	}

	/**特征规范化0-1法
	 * 同一列的每个特征减去该列特征的平均值，然后除以该列特征的标准方差
	 * */
	public static double[][] zeroOne(double[][] data){
		int r = data.length;
		int c = data[0].length;
		double[][] temp;//声明一个二维数组，用来存储处理过的数据
		temp = new double[r][c];
		for (int i = 0; i<r; i++){
			temp[i] = new double[c];
		}
		//求每一列的平均值和标准方差
		double[][] middata;
		middata = new double[r][c];
		middata[0] = new double[c];
		middata[1] = new double[c];
		for (int i = 0; i<c; i++){
			double[] mid = new double[r];
			for (int j = 0; j<r; j++)
			{
				mid[j] = data[j][i];
			}
			double midmean = mean(mid);
			double midstandarddeviation = standardDeviation(mid);
			middata[0][i] = midmean;
			middata[1][i] = midstandarddeviation;
		}
		//0-1法处理数据，存储在数组temp中返回
		for (int i = 0; i<c; i++){
			for (int j = 0; j<r; j++)
			{
				double b = data[j][i] - middata[0][i];
				temp[j][i] = b / middata[1][i];
			}
		}
		return temp;
	}
	/**
	 * 将某类特征值进行归一化
	 * @param lower 归一化的范围最低值
	 * @param upper 归一化的范围最高值
	 * @param value 要归一化的数据
	 * @param min 该类特征的最小值
	 * @param max 该类特征的最大值
	 * @return
	 */
	public static double zeroOneLibSvm(double lower,double upper,double value,double min,double max){
		return lower + (upper-lower) * (value-min)/(max-min);
	}
    //只能接收长度为128的数组 
    //若不足128补全为0
    public static double[] fft(double[] list) {
		int len=list.length;
		Complex[] theList = new Complex[list.length];
		for (int i = 0; i < len; i++) {
			theList[i] = new Complex(list[i], 0);
		}
		for (int i = len; i < 128; i++) {
			theList[i] = new Complex(0, 0);
		}
		
		// fft
		Complex[] Y = FFT.fft(theList);

		double alpha=1.0/(double)len;
		for (int i = 0; i < Y.length; i++) {
			Y[i] = Y[i].times(alpha);
		}	
		double[] fftSeries = new double[list.length/2];
		for (int i = 1, j = 0; i < list.length/2 + 1; i++, j++) {
			fftSeries[j] = 2 * Y[i].abs();
		}
	
		return fftSeries;
	}	
    
    /**
     * 均方根平均值
     * @param list
     * @return
     */
    public static double rms(double[] list){
    	double rms=0;
    	double sum = 0;
    	for(int i=0;i<list.length;i++){
    		sum+=Math.pow(list[i],2);
    	}
    	rms = Math.sqrt(sum/list.length);
    	return rms;
    }
    
    /**
     * 向量幅值面积
     * 把离散值面积累加起来然后除以总长度。实际是平均每时刻的面积。
     * 最后*1/T
     * @param list
     * @return
     */
    public static double sma(double[] data,double interval){
    	double sum=0;
    	double lot=data.length * interval;
    	for(int i=0;i<data.length;i++){
    		sum +=data[i]*interval;
    	}
    	return sum/lot;
    }
    
    /**
     * 四分卫距
     * @param list
     * @return
     */
    public static double iqr(double[] list){
    	return Features.thirdQuartile(list)-Features.firstQuartile(list);
    }
    /**
     * 绝对平均差
     * @return
     */
    public static double mad(double[] data){
		if(data == null || data.length == 0) return 0.0;
    	double mean = Features.mean(data);
    	double sum = 0;
    	for(int i=0;i<data.length;i++){
    		sum+=Math.abs(data[i]-mean);
    	}
    	return sum/data.length;
    }
    /**
     * 时域 能量
     * @return
     */
    public static double tenergy(double[] data){
		if(data == null || data.length == 0) return 0.0;
    	return Features.energy(data);
    }
    /**
     * 频域 标准备差
     * @return
     */
    public static double fdev(double[] data){
    	return Features.standardDeviation(data);
    }
    /**
     * 频域平均值
     * @return
     */
    public static double fmean(double[] data){
    	return Features.mean(data);
    }
   
    /**
     * 频域 偏度
     * @return
     */
    public static double skew(double[] data){
		if(data == null || data.length == 0) return 0.0;
    	double mean = Features.mean(data);
    	double dev = Features.standardDeviation(data);
    	double sum=0;
    	for(int i=0;i<data.length;i++){
    		sum+=Math.pow((data[i]-mean)/dev,3);
    	}
    	return sum/data.length;
    }
    
    /**
     * 频域 峰度
     * @return
     */
    public static double kurt(double[] data){
		if(data == null || data.length == 0) return 0.0;
    	double mean = Features.mean(data);
    	double dev = Features.standardDeviation(data);
    	double sum=0;
    	for(int i=0;i<data.length;i++){
    		sum+=Math.pow((data[i]-mean)/dev,4);
    	}
    	return sum/data.length-3;
    }
    public static void main(String[] args) {
//		double data[] = {10.878855766935272,10.908461119487015,10.81928196631571,10.58730748320247,10.232745023240879,9.820082035164127,9.446277208203828,9.219257427434261,9.231359230372695,9.533827547570272,10.11859569119169,10.91224331189776,11.784511688968529,12.570572809840332,13.103090746958674,13.247687737541641,12.934299977886454,12.177377558408873,11.07990629074698,9.819423583219514,8.617918529052174,7.700970051890311,7.253942667969325,7.383977846611197,8.095655756140488,9.285693051922491,10.75836990761661,12.25927641940615,13.521267191575948,14.313972953746614,14.48736830916101,14.000922897465884,12.932576422105498,11.465641804240178,9.855989045129858,8.385653894834617,7.311604443895332,6.819284736726312,6.989579832024596,7.785223451438029,9.058930701047279,10.581426510001666,12.08385485567902,13.306491913152898,14.044708796099366,14.183877545005728,13.717191658511275,12.743697408380989,11.447540333860413,10.062812888066755,8.830827502030335,7.957724521124835,7.579921220742641,7.743166223508059,8.398273061675393,9.413509186629291,10.600694811778286,11.749831465650136,12.665885101500308,13.201333238403585,13.279170061969658,12.902981147705825,12.153061552945251,11.169926074119683,10.128553544895778,9.208023034748251,8.561681695515247,8.292622742452114,8.43817043518426,8.965486544765287,9.778601375571444,10.73541058174451,11.671713742145444,12.428389439394742,12.877416480419246,12.942693026448127,12.61242761555075,11.941161389467545,11.04105541882187,10.063727248032263,9.175415028929113,8.529362668260086,8.239870439479493,8.362326457836929,8.882703958689932,9.718566199911697,10.731762970563283,11.751016549465609,12.600814983180577,13.131783742707773,13.24725110755765,12.921191126899787,12.204091587094663,11.215352802058256,10.123229646660278,9.115648036471597,8.367012569929425,8.00700775559153,8.097172097442025,8.61969211917407,9.480642177664455,10.52719206304871,11.575636304081854,12.444995658112699,12.989833468804912,13.126058235202649,12.844843614543509,12.21213095507425,11.354025265632734,10.431178322218262,9.607402902062763,9.018835933551912,8.749742963189995,8.81958059933126,9.183520572146033,9.745801677481992,10.382618371500179,10.96934290439437,11.406116019673721,11.636383536023402,11.65467159127098,11.50239347188893,11.253204525158896,10.991758421863512,10.79114452561111,10.694483163021173,10.705081217588043,10.78745131944964};
    	double data[] = {1.0115,1.0078,1.0101,1.012,1.0086,1.0026,1.0005,0.99946,0.99495,0.99215,0.99384,0.99699,1.0052,1.0038,1.0024,0.98682,0.98073,0.97632,0.96349,0.96084,0.95941,0.94998,0.9524,0.9655,0.97898,0.99573,0.99052,0.99882,1.0092,1.0217,1.0251,1.0226,1.0178,1.0174,1.0046,0.99308,0.97619,1.0046,1.0368,1.0667,1.0848,1.0427,0.99318,0.95382,0.97589,1.0598,1.1838,1.2641,1.2626,1.1942,1.1143,1.0326,0.96903,0.9302,0.9057,0.89961,0.9436,1.0141,1.0479,1.046,1.023,0.98699,0.96196,0.92306,0.87476,0.8552,0.87011,0.90212,0.92728,0.95041,0.97124,0.98365,1.0036,1.0282,1.0216,0.99734,0.97715,0.98086,1.003,1.025,1.046,1.0469,1.0249,1.0176,1.0411,1.0691,1.1077,1.1741,1.2727,1.3035,1.236,1.194,1.1576,1.1544,1.1871,1.1339,1.0192,0.98102,1.0501,1.1415,1.2091,1.1086,0.94534,0.83076,0.76927,0.73377,0.70416,0.69854,0.73205,0.80718,0.89695,0.96747,0.98242,0.95086,0.9073,0.8872,0.88047,0.8977,0.97157,1.0611,1.1339,1.2016,1.2409,1.1728,1.1278,1.2244,1.4193,1.3436,1.2391,1.2839,1.2785,1.192,1.0069,0.77816,0.67482,0.76429,0.98892,1.1273,1.1022,0.98458,0.8869,0.8139,0.76918,0.75682,0.80862,0.90737,1.0268,1.1129,1.1258,1.0548,0.97597,0.89862,0.85669,0.88036,0.92533,0.98641,1.0728,1.0771,1.0896,1.1279,1.1958,1.3483,1.4987,1.3608,1.17,1.0362,1.0187,1.0316,1.0236,1.0441,1.0883,1.1746,1.2385,1.2176,1.0549,0.91635,0.8221,0.7831,0.72136,0.68519,0.69557,0.73922,0.77843,0.83116,0.88034,0.934,0.95142,0.97277,0.97559,0.95326,1.0155,1.1168,1.1773,1.2577,1.2977,1.3745,1.681,1.5653,1.5535,1.4195,1.0903,0.78526,0.56338,0.65767,0.94608,1.1601,1.1658,0.99196,0.83522,0.78645,0.80042,0.88384,0.935,0.92008,0.91707,0.91917,0.93769,0.95041,0.91512,0.88908,0.89089,0.94693,0.97036,1.0172,1.0784,1.1379,1.1557,1.1765,1.1828,1.209,1.2745,1.1729,1.0327,1.0747,1.1446,1.1615,1.1818,1.1215,0.95672,0.90727,1.0191,1.0832,1.1367,1.0393,0.90471,0.83961,0.7858,0.79058,0.75159,0.75867,0.82857,0.91818,0.9751,1.0055,0.96165,0.92374};
    	System.out.println(Features.energy(data));
		
		double[] fft = Features.fft(data);
		System.out.println("");
		/*System.out.print("fft:");
		for(int i=0;i<fft.length;i++){
			System.out.print(fft[i]+",");
		}
		System.out.println();
		System.out.println("spp："+Features.spp(fft));
		System.out.println("entropy："+Features.entropy(fft));
		System.out.println("energy："+Features.energy(fft));
		System.out.println("centroid："+Features.centroid(fft));*/
	}
}