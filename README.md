# MVC_Retrofit_Android
MVC设计模式和Retrofit框架在Android项目中的应用

#先看项目演示
![image](https://github.com/fanjianli/YanShi/blob/master/MVC_yanshi.png)

##
#MVC

	视图（View）：用户界面。
	控制器（Controller）：业务逻辑。
	模型（Model）：数据保存。
如下图很直观的展示了MVC框架的核心：

![image](https://github.com/fanjianli/YanShi/blob/master/mvc.png)
	View传送指令到Controller，Controller完成业务逻辑后，要求Model改变状态，Model将新的数据发送到View，用户得到反馈。

##
#应用
  架构图：
![image](https://github.com/fanjianli/YanShi/blob/master/MVC_.png)
MainActivity实现发送网络请求的接口，和联网结果处理的接口，并对modle和view声明引用和实例化，接口的作用是体现出多态的概念。（个人认为接口可以当作连接mvc的纽带）


	public class MainActivity extends AppCompatActivity implements RequestWeatherView,OnWeatherListener{
    private MainView mainView;//对应的View层
    private static MainModle mainModle = null;//对应的Modle抽象类
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainView = new MainView(this,this);//实例化View
        if(null == mainModle){
            mainModle = new MainModelImpl();//实例化抽象，多态
        }
    }

    @Override
    public void onSuccess(Weather weather) {
       mainView.showSuccess(weather);
    }

    @Override
    public void onError() {
        mainView.showFailed();
    }

    @Override
    public void sendRequest(String num) {
        if(NetWorkUtil.isNetWorkConnected(this)) {
            mainModle.getWeather(this, num, this);
        }else {
            mainView.showNoNet();
        }
    }
	}

View层做一些View改变的函数和初始化以及监听事件：

	public class MainView {
    private MainActivity activity;
    RequestWeatherView iRequest;
    private EditText editNum;
    private Button btnGo;
    private LoadingView loadding;
    private TextView showText;

    public MainView(MainActivity activity,RequestWeatherView iRequest){
        this.activity = activity;this.iRequest = iRequest;
        initView();
    }
    public void btn_go(){
        loadding.setState(LoadingState.STATE_LOADING);
        loadding.setVisibility(View.VISIBLE);
        showText.setVisibility(View.GONE);
        iRequest.sendRequest(editNum.getText().toString().trim());
    }
    private void initView() {
        editNum = (EditText) activity.findViewById(R.id.edit_num);
        btnGo = (Button) activity.findViewById(R.id.btn_go);
        loadding = (LoadingView) activity.findViewById(R.id.loadding);
        showText = (TextView) activity.findViewById(R.id.show_text);
        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_go();
            }
        });
        loadding.withLoadedEmptyText("≥﹏≤ , 连条毛都没有 !").withEmptyIco(R.mipmap.disk_file_no_data).withBtnEmptyEnnable(false)
                .withErrorIco(R.mipmap.ic_chat_empty).withLoadedErrorText("(῀( ˙᷄ỏ˙᷅ )῀)ᵒᵐᵍᵎᵎᵎ,我家程序猿跑路了 !").withbtnErrorText("去找回她!!!")
                .withLoadedNoNetText("你挡着信号啦o(￣ヘ￣o)☞ᗒᗒ 你走").withNoNetIco(R.mipmap.ic_chat_empty).withbtnNoNetText("网弄好了，重试")
                .withLoadingIco(R.drawable.loading_animation).withLoadingText("加载中...").withOnRetryListener(new OnRetryListener() {
            @Override
            public void onRetry(){
                btn_go();
            }
        }).build();
        loadding.setVisibility(View.GONE);
        showText.setVisibility(View.GONE);
    }
    public void showSuccess(Weather w){
        loadding.setVisibility(View.GONE);
        showText.setVisibility(View.VISIBLE);
        showText.setText(w.toString());
    }
    public void showFailed(){
        loadding.setState(LoadingState.STATE_ERROR);
        loadding.setVisibility(View.VISIBLE);
        showText.setVisibility(View.GONE);
    }
    public void showNoNet(){
        loadding.setState(LoadingState.STATE_NO_NET);
        loadding.setVisibility(View.VISIBLE);
        showText.setVisibility(View.GONE);
    }
	}


Modle类实现接口并利用联网后操作接口通知Activity做出改变


	public class MainModelImpl implements MainModle{
    private static final String TAG = "MainModelImpl";
    /**
     * 实现MainModole获取天气信息
     * Created by fan on 2016/10/16.
     * cityNum:城市代码
     * listener:网络连接是否成功接口
     */
    @Override
    public void getWeather(Context context, String cityNum, final OnWeatherListener listener) {
        ApiManager.getWeather(cityNum)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Weather>() {
            @Override
            public void call(Weather weather) {
                Log.e(TAG, "MainModelImpl:" + weather);
                listener.onSuccess(weather);
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                Log.e(TAG, "MainModelImpl:" + throwable);
                listener.onError();
            }
        });
    }
	}

#主要代码
*MainActivity:


  		mainView = new MainView(this,this);//实例化View
        if(null == mainModle){
            mainModle = new MainModelImpl();//实例化抽象，多态,连接M和C,C通知M获取数据等操作
        }
*MainView:
	
	RequestWeatherView iRequest;//接口连接V和C，用于通知C层做操作
	public MainView(MainActivity activity,RequestWeatherView iRequest){
        this.activity = activity;this.iRequest = iRequest;
        initView();
    }

*Model:
 	通过接口连接M和View

    public void getWeather(Context context, String cityNum, final OnWeatherListener listener) {
        ApiManager.getWeather(cityNum)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Weather>() {
            @Override
            public void call(Weather weather) {
                Log.e(TAG, "MainModelImpl:" + weather);
                listener.onSuccess(weather);
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                Log.e(TAG, "MainModelImpl:" + throwable);
                listener.onError();
            }
        });
    }

#Retrofit2

  	private static final String STARTURL = "http://apis.baidu.com";
    private static final Retrofit getServiceList = new Retrofit.Builder()
            .baseUrl(STARTURL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create()) // 使用RxJava作为回调适配器
            .build();
   	private static final ApiService apiManager = getServiceList.create(ApiService.class);
    public static Observable<Weather> getWeather(String pinyin){
        return apiManager.getWeather("c50757d2e4d31f64290d503c74fe6054",pinyin);
    }


	public interface ApiService {


    @GET("/apistore/weatherservice/weather")
    Observable<Weather> getWeather(@Header("apikey") String apikey, @Query("citypinyin") String pinyin);
	}
