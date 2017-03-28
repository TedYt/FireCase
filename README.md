# FireCase
#高德地图
## 定位类型aMapLocation.getLocationType()
###定位类型：
http://lbs.amap.com/api/android-location-sdk/guide/utilities/location-type/
0                请通过AMapLocation.getErrorCode()方法获取错误码，并参考错误码对照表进行问题排查。
1   GPS定位结果   通过设备GPS定位模块返回的定位结果，精度较高，在10米－100米左右
2   前次定位结果   网络定位请求低于1秒、或两次定位之间设备位置变化非常小时返回，设备位移通过传感器感知。
4   缓存定位结果   返回一段时间前设备在同样的位置缓存下来的网络定位结果
5   Wifi定位结果   属于网络定位，定位精度相对基站定位会更好，定位精度较高，在5米－200米之间。
6   基站定位结果   纯粹依赖移动、连通、电信等移动网络定位，定位精度在500米-5000米之间。
8   离线定位结果
###定位错误码：
http://lbs.amap.com/api/android-location-sdk/guide/utilities/errorcode/

#------------------------------------------------
#视频
如果使用hardlibrary硬解库中的视频录制api，实际使用华为荣耀8测试，支持格式参数如下：
//        mPublisher.setOutputResolution(640, 480);//vga效果不错款
//        mPublisher.setOutputResolution(480, 640);//vga效果不错窄
//        mPublisher.setPreviewResolution(704, 576);//4cif
//        mPublisher.setOutputResolution(704, 576);//4cif 不支持
//        mPublisher.setPreviewResolution(720, 576);//4cif
//        mPublisher.setOutputResolution(720, 576);//4cif不支持
//        mPublisher.setPreviewResolution(800, 600);//svga
//        mPublisher.setOutputResolution(800, 600);//svga不支持
//        mPublisher.setPreviewResolution(1024, 768);//svga
//        mPublisher.setOutputResolution(1024, 768);//svga不支持
//        mPublisher.setPreviewResolution(1280, 720);//720p
//        mPublisher.setOutputResolution(1280, 720);//720p
//        mPublisher.setPreviewResolution(1920, 1080);//1080p
//        mPublisher.setOutputResolution(1920, 1080);//1080p
//        mPublisher.setOutputResolution(1080, 1920);//1080p太细了
//        mPublisher.setOutputResolution(640, 480);//**这个效果好些**
##########################################################################