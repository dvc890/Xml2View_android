## Xml2View for android

* **支持自定义View**
* **在Assets下模拟R.id环境**
* **支持include字段**
* **支持Xml格式的Drawable资源**

## 如何获取布局中的View对象

###  方法1：

	public class testActivity extends Activity {

        @XmlDynamicViewId(id = "ok_btn")
        public RoundButton ok_btn;
        public RoundButton cancel_btn;

        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            View sampleView = XmlDynamicView.createView(this, "layout/example2.xml", this);
            cancel_btn = (RoundButton)sampleView.findViewById(ID.getID("cancel_btn"));
            ok_btn.setOnClickListener(this);
            cancel_btn.setOnClickListener(this);
        }
	}

	
###  方法2：
最后的参数直接传入null，从tag中得到id列表，然后通过id列表去findViewById

	View sampleView = XmlDynamicView.createView(this, "layout/example.xml", null);
	HashMap<String, Integer> idsMap = (HashMap<String, Integer>)sampleView.getTag();
	RoundButton ok_btn = (RoundButton)sampleView.findViewById(idsMap.get("ok_btn"));
	holder.ok_btn.setOnClickListener(this);

###  方法3：
1.先定义一个类，格式如下，如果是内部类，需要加上`static`

	public static class SampleViewHolder {
	    @XmlDynamicViewId(id = "ok_btn")
	    public RoundButton ok_btn;
	    public SampleViewHolder() {}
	}


2.将该类在创建View的时候作为参数传入，然后由此可以得到所有你所需要的view

	View sampleView = XmlDynamicView.createView(this, "layout/example.xml", SampleViewHolder.class);
	holder = (SampleViewHolder) sampleView.getTag();
	holder.ok_btn.setOnClickListener(this);

