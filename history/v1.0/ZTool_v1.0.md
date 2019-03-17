#Summary
* Connect
* Tool

</br>
</br>

---

</br>
</br>


##Connect
> 1. ZConnect


##### 1. ZConnect :


###### callback Class
    
    /***
     *  連線接口
     */
	public class ZConnectHandler {
	
	
	    //-----------
	
	    /***
	     *  適用inputstream接口
	     * @param inputStream
	     * @param code
	     */
	    public void OnInputStreamResponse(InputStream inputStream,int code) 
	
	    
	    //-----------
	
	    /***
	     * 適用 jsonString 接口
	     * @param response
	     * @param code
	     */
	    public void OnStringResponse(String response,int code) 
	
	    
	    //-----------
	
	    /***
	     *  連線失敗接口
	     */
	    public void OnFail() 
	
	}



     
###### Public Method




*	支援的上傳檔案類型</br>
  @uploadFileType</br>


 		_FILE_TYPE_JPG = "image/jpg"
		_FILE_TYPE_PNG = "image/png"
 		_FILE_TYPE_CSV = "text/csv"








* public void setCustomLoadingDialog(@Nullable Dialog dialog)

	> 自訂客製化dialog</br>
	> 如不欲顯示LoadingDialog 將參數設為null即可</br>
	> 不使用此方法則會以預設Loading執行</br>
	
	 //------------------




* public void setConnectTimeOut(int connectTimeOut)

	> connectTimeOut 連線time out 單位毫秒

	 //------------------


* public void setWriteTimeOut(int writeTimeOut)

	> writeTimeOut 寫入time out 單位毫秒

	//----------------


* public void setReadTimeOut(int readTimeOut) 

	> readTimeOut 讀取time out 單位毫秒</br>




	//------------------



* public void get(String apiURL, ZConnectHandler zConnectHandler) 

	> /***</br>
	> * 使用情境:</br>
	> * 1.http get </br>
	> *  ... ... ... ... ... ...</br>
	> * apiURL url</br>
	> * zConnectCallback callback</br>
	> */</br>


		Sample :

        zConnect.get("your url", new ZConnectHandler() {
            /**
            *  onFail
            */
            @Override
            public void OnFail() {
                super.OnFail();
            }

            /***
            *  當回傳串流
            * @param inputStream
            * @param code 狀態碼
            */
            @Override
            public void OnInputStreamResponse(InputStream inputStream, int code) {
                super.OnInputStreamResponse(inputStream, code);
            }

            /***
            *  當回傳字串
            * @param response
            * @param code 狀態碼
            */
            @Override
            public void OnStringResponse(String response, int code) {
                super.OnStringResponse(response, code);
            }
        });

	//------------------


* public void get(String apiURL, Map<String, String> headers, ZConnectHandler zConnectHandler) 


	> 使用情境 :</br>
	> 1. http get</br>
	> 2. params</br>
	> ... ... ... ... ... ...</br>
	> apiURL url</br>
	> headers header 用不到則帶null即可</br>
	> zConnectHandler callback

		Sample :

        Map<String,String> headers = new HashMap<>();
        headers.put("token","AABBCCDDEEFFGGHHIIJJKKLL");

        zConnect.get("your url",headers, new ZConnectHandler() {
            /**
             * onFail
             */
            @Override
            public void OnFail() {
                super.OnFail();
            }

            /***
             * 當回傳串流
             * @param inputStream
             * @param code 狀態碼
             */
            @Override
            public void OnInputStreamResponse(InputStream inputStream, int code) {
                super.OnInputStreamResponse(inputStream, code);
            }

            /***
             * 當回傳字串
             * @param response
             * @param code 狀態碼
             */
            @Override
            public void OnStringResponse(String response, int code) {
                super.OnStringResponse(response, code);
            }
        });
    


	//-----------------


* public void post(String apiURL, @Nullable Map<String, String> headers, @Nullable Map<String, String> params, ZConnectHandler zConnectHandler) 


	> 使用情境 :</br>
	> 1.http post</br>
	> 2.header</br>
	> 3.params</br>
	> ... ... ... ... ... ...</br>
	> apiURL url</br>
	> headers header 用不到則帶null即可</br>
	> params key_Value參數 用不到則帶null即可</br>
	> zConnectHandler callback

		Sample :

        Map<String, String> headers = new HashMap<>();
        headers.put("token", "AABBCCDDEEFFGGHHIIJJKKLL");

        Map<String, String> params = new HashMap<>();
        params.put("name", "noel");
        params.put("age", "26");
        params.put("sex", "male");
        
        zConnect.post("your url", headers, params, new ZConnectHandler() {
            /**
             *  onFail
             */
            @Override
            public void OnFail() {
                super.OnFail();
            }

            /***
             *  當回傳串流
             * @param inputStream
             * @param code 狀態碼
             */
            @Override
            public void OnInputStreamResponse(InputStream inputStream, int code) {
                super.OnInputStreamResponse(inputStream, code);
            }

            /***
             *  當回傳字串
             * @param response
             * @param code 狀態碼
             */
            @Override
            public void OnStringResponse(String response, int code) {
                super.OnStringResponse(response, code);
            }
        });




	//-----------------


* public void post(String apiURL, @Nullable Map<String, String> headers, Object requestModel, ZConnectHandler zConnectHandler) 


	> 使用情境:</br>
	> 1. http post</br>
	> 2. header</br>
	> 3. json request</br>
	> ... ... ... ... ... ...</br>
	> apiURL url</br>
	> headers header 用不到則帶null即可</br>
	> requestModel  json object model  用不到則帶null即可</br>
	> zConnectHandler callback

		Sample:


		public class PersonModel(){
			private String name;
			private String age;
			private String sex;
			
			public PersonModel(String name,String age,String sex){
				this.name = name;
				this.age = age;
				this.sex = sex;
			}
		}

		//------

        zConnect.post("your url", headers, params,new PersonModel("noel","26","male") ,new ZConnectHandler() {
            /**
             *  onFail
             */
            @Override
            public void OnFail() {
                super.OnFail();
            }

            /***
             *  當回傳串流
             * @param inputStream
             * @param code 狀態碼
             */
            @Override
            public void OnInputStreamResponse(InputStream inputStream, int code) {
                super.OnInputStreamResponse(inputStream, code);
            }

            /***
             *  當回傳字串
             * @param response
             * @param code 狀態碼
             */
            @Override
            public void OnStringResponse(String response, int code) {
                super.OnStringResponse(response, code);
            }
        });






	//----------------

* public void post(String apiURL, @Nullable Map<String, String> headers, @Nullable Map<String, String> params, String fileKey, String fileName, File file, @uploadFileType String fileType, ZConnectHandler zConnectHandler) 

	> 使用情境:</br>
	> 1.http post</br>
	> 2.上傳檔案</br>
	> 3.params</br>
	> 4.header</br>
	> ... ... ... ... ... ...</br>

	> apiURL url</br>
	> headers headers 用不到則帶null即可</br>
	> params params 用不到則帶null即可</br>
	> fileKey 後台此欄位key值</br>
	> fileName 檔名</br>
	> file 檔案</br>
	> fileType 檔案類型 參考 @uploadFileType</br>
	> zConnectHandler callback


		Sample:

        Map<String, String> headers = new HashMap<>();
        headers.put("token", "AABBCCDDEEFFGGHHIIJJKKLL");

        Map<String, String> params = new HashMap<>();
        params.put("name", "noel");
        params.put("age", "26");
        params.put("sex", "male");
        

        zConnect.post("your url", headers, params,"file","fileName.jpg",new File("path"),_FILE_TYPE_JPG, new ZConnectHandler() {
            /**
             *  onFail
             */
            @Override
            public void OnFail() {
                super.OnFail();
            }

            /***
             *  當回傳串流
             * @param inputStream
             * @param code 狀態碼
             */
            @Override
            public void OnInputStreamResponse(InputStream inputStream, int code) {
                super.OnInputStreamResponse(inputStream, code);
            }

            /***
             *  當回傳字串
             * @param response
             * @param code 狀態碼
             */
            @Override
            public void OnStringResponse(String response, int code) {
                super.OnStringResponse(response, code);
            }
        });



	//---------------


* public void post(String apiURL, @Nullable Map<String, String> headers, @Nullable Object requestModel, String fileKey, String fileName, File file, @uploadFileType String fileType, ZConnectHandler zConnectHandler)

	> 使用情境:</br>
	> 1.http post</br>
	> 2.上傳檔案</br>
	> 3.json request</br>
	> 4.header</br>
	>... ... ... ... ... ...</br>
	>
	> apiURL           url</br>
	> headers          header 用不到則帶null即可</br>
	> requestModel     requestModel 用不到則帶null即可</br>
	> fileKey          後台此欄位key值</br>
	> fileName         檔名</br>
	> file             檔案</br>
	> fileType         檔案類型 參考 @uploadFileType</br>
	> zConnectHandler callback



		Sample:


		public class PersonModel(){
			private String name;
			private String age;
			private String sex;
			
			public PersonModel(String name,String age,String sex){
				this.name = name;
				this.age = age;
				this.sex = sex;
			}
		}

        Map<String, String> headers = new HashMap<>();
        headers.put("token", "AABBCCDDEEFFGGHHIIJJKKLL");



        zConnect.post("your url", headers, new PersonModel("noel","26","male"),"file","fileName.jpg",new File("path"),_FILE_TYPE_JPG, new ZConnectHandler() {
            /**
             *  onFail
             */
            @Override
            public void OnFail() {
                super.OnFail();
            }

            /***
             *  當回傳串流
             * @param inputStream
             * @param code 狀態碼
             */
            @Override
            public void OnInputStreamResponse(InputStream inputStream, int code) {
                super.OnInputStreamResponse(inputStream, code);
            }

            /***
             *  當回傳字串
             * @param response
             * @param code 狀態碼
             */
            @Override
            public void OnStringResponse(String response, int code) {
                super.OnStringResponse(response, code);
            }
        });









</br>
</br>


---

</br>
</br>

##Tool
> 1. ZFileTool
> 2. ZTimeTool
> 3. ZConvertUnitTool
> 4. ZCheckFormatTool
> 5. ZCheckDeviceTool




##### 1. ZFileTool

###### Public Method


* public void saveImageToInternalStorage(Bitmap bitmap, Bitmap.CompressFormat imageType, String imageName)


	> 將圖片檔以指定格式存取於內部資料夾(須包含副檔名)</br>
	> bitmap     將指定bitmap儲存於內部資料夾</br>
	> imageType  欲儲存的圖片檔之副檔名(需一致,png、jpg等)</br>
	> imageName  將此此檔案以此名稱(檔名+副檔名)儲存於內部資料夾</br>





	//---------------




* public Bitmap getImageFromInternalStorage(String fileName) 


 	> 將存於內部資料夾的圖片檔以Bitmap取出</br>
 	> 如檔案不存在則取得null</br>
 	> fileName  目標檔名+副檔名</br>



	//---------------

* public void saveFileToInternalStorage(String fileName, byte[] data) 


 	> 存取檔案於內部資料夾</br>
 	> fileName   將此此檔案以此名稱(檔名+副檔名)儲存於內部資料夾</br>
 	> data       Image to byte[]</br>
 	

	//---------------


* public File getFIleFromInternalStorage(String fileName) 

	> 讀取內部資料夾中的檔案以File取出</br>
	> 如檔案不存在則取得null</br>
	> fileName  目標檔名+副檔名</br>


	//----------------


* public String getAssetsFileToString(String fileName) 


	> 讀取 assets中的檔案為String</br>
	> fileName  目標檔名+副檔名</br>





##### 2. ZTimeTool

###### Public Method


* public boolean isRange(long beginTime, long endTime) 


	> 目前時間是否在時段內</br>
	> @param beginTime  範圍開始時間</br>
	> @param endTime  範圍結束時間</br>



    //-------------------

* public boolean isBefore(long targetTime) 


	> 目前時間是否在指定時間之前</br>
	> targetTime</br>


	//-----------------


* public boolean isAfter(long targetTime) 


	> 目前時間是否在指定時間之後</br>
	> targetTime</br>




##### 3. ZConvertUnitTool

###### Public Method


* public float convertDpToPx(float dp) 


	> dp 轉 px</br>
	> dp 原始dp</br>


	//-----------------




* public float convertPxToDp(float px)

	> px 轉 dp</br>
	> px 原始px</br>

	//-----------------


* public float convertSpToPx(float sp) 

	> sp 轉 px</br>
	> sp   原始sp</br>


    //----------

* public float convertPxToSp(float px) 


	> px 轉 sp</br>
	> px   原始px</br>



    //-----------


* public float convertSpToDp(float sp)

	> sp 轉 dp</br>
	> sp   原始sp</br>

    //---------


* public float convertDpToSp(float dp) 

	> dp 轉 sp</br>
	> dp   原始dp</br>





##### 4. ZCheckFormatTool

###### Public Method


* public boolean isNumber(String text) 

	> 是否都是數字</br>
	> 目標字串</br>


    //-------


* public boolean isChinese(String text) 

	> 是否都是中文</br>
	> 目標字串</br>

    //-----------


* public boolean isEnglish(String text) 

	> 是否都是英文</br>
	> 目標字串</br>


    //-----------


* public boolean isUpperEnglish(String text) 

	> 是否都是 大寫英文</br>
	> 目標字串</br>


    //-----------


* public boolean isLetterEnglish(String text) 

	> 是否都是 小寫英文</br>
	> 目標字串</br>

    //-----------

* public boolean isEmail(String text) 

	> 是否為Email格式</br>
	> 目標字串</br>


##### 5. ZCheckDeviceTool

###### Public Method


* public String getMacAddress() 

	> 取得唯一碼  MacAddress</br>
	> 如果使用MacAddress 時裝置须具有上網功能</br>


    //--------------


* public String getAndroidID()

	> 取得唯一碼 Android ID</br>
    > 如果手機恢復原廠設定 此ID將改變</br>


	--------------


* public boolean isHasBehindCamera() 

	> 是否具備後鏡頭

    //------------


* public boolean isHasFrontCamera() 

	> 是否具備前鏡頭

    //-----------


* public boolean isHasFlash() 

	> 是否具備閃光燈

    //----------


* public boolean isHasMicrophone() 

	> 是否具備錄音麥克風

    //------------


* public boolean isHasGPS() 

	> 是否具備GPS

    //--------------

* public boolean isHasFingerPrint() 
	>  是否具備指紋辨識</br>
	>  Android 系統版本 API Level 23以上開始支援指紋辨識 以下的版本都取得false</br>

    //----------------

* public boolean isHasBarometer() 
	> 是否具備氣壓傳感器


    //----------------


* public boolean isHasTemperature() 

	> 是否具備溫度傳感器</br>
 	> Android 系統版本 API Level 21以上開始支援指紋辨識 以下的版本都取得false</br>

    //----------------

* public boolean isHasCompass() 

	> 是否具備指南針

    //---------------

* public boolean isHasGyroscope() 
	
	> 是否具備陀螺儀
	 
    //----------------


* public boolean isHasBlueTooth()

	> 是否具備藍芽
    //---------------




* public boolean isHasBLE()
	> 是否具備低功耗藍芽


