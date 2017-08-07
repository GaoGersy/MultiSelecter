###### 先上图，看看今天开什么车，坐稳咯。拖拉机即将超速行使，请系好安全带！
![MultiSelector.gif](http://upload-images.jianshu.io/upload_images/2673171-568b55a3bb4190c9.gif?imageMogr2/auto-orient/strip)
>先来分析一下android中会遇到哪些选择方面的需求：
  1. 单选--这个就不啰嗦了
  2. 多选：
      1. 全部数据都可以被选择，这个比较简单
      2. 过滤掉一部分数据，不让显示出来
      3. 过滤掉一部分数据，能显示，但用户不能选择
      4. 默认选择一部分数据，用户可以取消选择
      5. 默认选择一部分数据，用户不可以取消选择
>原谅我还只遇到过这些，那这里都有哪些功能呢？

   1. 多选时会有选择过渡动画（如GIF里所见，但肯定不能肯的这样掉渣的）
    2. 点击上面的头像可以取消选择
    3. 选中的数据条数提示
    4. 根据选择的条目会将搜索栏向右移动，搜索样最少占屏幕宽度的1/3
    5. 当然还有模糊搜索功能啦
    6. 全选
>啰啰嗦嗦讲了一堆，看一下Gif图就什么都知道了好么？
好吧，遇到像你们这些高智商的，还是直接开车好了

###### 一、缘起：
早上9点，程序猿小猿同学一如往常准时的坐在自己的位置上，又是燥热的一天。挤了一个小时的地铁，原本瘦消的身体越发觉得扁平了。小猿心想：我要再挤个一年，也会成A4腰哦，不是像A4纸那么宽，而是像它那么扁。开机-洗水杯-吃早餐，小猿开启了程式化的一天。

小猿刚叼起一个小笼包，还没来得及体会里面的美味，产品经理轻轻拍了下他的肩膀。直勾勾的看着小猿眼睛说：“昨天那个选择页面要做一个已经选择条目头像展示的功能。”小猿刚想说我cao,产品经理连忙说，中午下班前看下效果。小猿差点没把小笼包整个咽下去，早TM干吗去了，昨天晚上加班赶出来的，又得改，操蛋玩意儿，小猿心理骂道。

快速解决了小笼包，迅速进入到战斗状态。好在这个不是太难实现，只是在数据传递方面要下点功夫。在饭点前总算一切搞掂，翘着二郎腿对产品说，你要的头像展示功能OK了。产品听完悠悠的转过身，肯定道：嗯嗯，不错，是我想要的。不过感觉好像还少了点什么，哦，对了，这个页面条目太多，用户可能看不过来，需要一个搜索功能。放在这些头像同一排就好了，就微信那样。说完拍拍小猿的肩膀，跟其它同事出去吃饭去了，留下小猿在风中凌乱。

小猿点了个外卖，然后想着刚刚这个需求要怎么实现。突然小猿用力的拍了一下桌子，这样下去不是办法，以后还哪里敢吃小笼包，只能喝粥度日了。我得赶在那货之前弄个比较全面的解决方案，小猿对自己斩钉截铁的说到。

小猿在想着他的对策，首先不能跟某一块耦合了，万一其它地方也要用到呢，那不是尴尬了？功能得尽可能的全面些，多选，数据过滤，单选....甚至还可以来得动画。小猿仿佛看到了自己对于需求应对自如的样子....一丝冷风吹过，吹醒了正在做白日梦的小猿。
###### 二、 抽象：
说干就干，首先得解决的是如何解耦，得用接口，接口是对现实的抽象。有了思路剩下的就是手速了，像小猿这种单身20多年的，手速当然快如闪电，要不能上砖石？
  1. 先造一个能判断数据是何种选择类型的接口，返回不同条目类型（可选、不可选 ....），像这样：
```
public interface Filter {

    //专为标题而生，因为系统默认给的是0，这样title Bean类里面都不用做过多的修改了
    int TITLE_NO_CHOICE = 0;

    //默认不选中，可以选中
    int NORMAL = 1;

    //默认选中,不可以取消选中
    int SELECTED_NOCANCEL = 2;

    //默认不选中,不能被选中
    int NO_CHOICE = 3;

    //不显示在列表
    int NOT_SHOW = 4;

    //本地图片地址
    int getImageResource();

    //网络图片url
    String getImageUrl();

    //返回当前条目的状态，就是上面定义的那些个常量，返回值会在BaseViewHolder里面用到
    int filter();

    //是否是选中状态
    boolean isSelected();

    void setSelected(boolean isSelected);

    //是否匹配搜索关键字，用来处理搜索的，如果不要搜索功能，可以不用处理
    boolean isMatch(String condition);
}
```
>这样只要每个具体bean 类去实现
在filter方法里去根据不同的条件返回上面定义的常量
isMatch方法是针对模糊搜索设计的
机智如我，看你还怎么改需求，小猿暗自窃喜...

  2. 图片加载框架现在有好几个，搞不好哪个以后就不维护了，我可不能在一棵树上吊死，小猿警惕起来，顺手撸了个图片加载框架的接口：
```
public interface IImageLoader {
    void showImage(Context context, String url, ImageView imageView);
}
```
>以后项目想用哪个图片框架只需要根据当前项目使用的图片加载框架实现对应的ImageLoader就可以了，就是这么任性

###### 三、实施：
大致思路是有了，具体要怎么实现这些功能呢？小猿摸着日益上扬的发迹线，沉思良久....
  1. 不需要展示给用户的数据过滤倒是好做,可以像下面这样的呀：
```
protected List<T> getFilterItems(List<T> items) {
        mSelectionList.clear();
        if (items != null) {
            List<T> data = new ArrayList<>();
            for (T item : items) {
                int type = item.filter();
                if (type != Filter.NOT_SHOW) {
                    data.add(item);
                    if (type != Filter.NO_CHOICE&&type!=Filter.TITLE_NO_CHOICE) {
                        mSelectionList.add(item);
                    }
                }
            }
            return data;
        } else {
            return null;
        }
    }
```
  2. 如果是正常的数据，既要支持多选又能支持单选，这个要怎么处理比较好呢？毫无头绪，一不小心干掉几根本来就屈指可数的头发，抱着试试的心态写了下面的代码：
```
  public void setData(T data) {
        normalBackgroundResource = getNormalBackgroundResource();
        noChoiceBackgroundResource = getNoChoiceBackgroundResource();
        int type = data.filter();
        if (mCheckBox != null) {
            mCheckBox.setChecked(data.isSelected());
        }
        if (type == Filter.NORMAL) {
            onNormal(data);
        } else if (type == Filter.NO_CHOICE) {
            onNoChoice();
        }
    }

  private void setClickListener(final Filter data) {
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectType == SelectionFragment.MULTI_SELECT) {
                    multiSelect(data);
                } else {
                    singleSelect(data);
                }
            }
        });
    }

   private void singleSelect(Filter data) {
        if (mListener == null) {
            throw new IllegalStateException("没有设置点击事件监听");
        }
        if (mCurrentCheckBox != null) {
            mCurrentCheckBox.setChecked(false);
            mCurrentItem.setSelected(false);
            mListener.onItemClick(itemView, mCurrentItem, false);
        }
        data.setSelected(true);
        mCheckBox.setChecked(true);
        mListener.onItemClick(itemView, data, true);
        mCurrentCheckBox = mCheckBox;
        mCurrentItem = data;
    }
```
>写完心理在打鼓，不知道好不好使，反正试下又不要钱。经过漫长的等待，程序总算部署完毕，这么个小项目编译居然还两分钟，还天天嫌我效率低，小猿抱怨道。试了下单选功能，Prefect！简直不要太如我意，哈哈。尝到甜头的小猿准备再试试多选功能，我草，还是那么完美。机智如我，啦啦啦....差点唱起了歌

  3 . 那如果是需要展示，但是不能操作的呢？这个就简单了，随手就撸了一个出来：
```
   public void onNoChoice() {
        if (mCheckBox != null) {
            mCheckBox.setBackgroundResource(noChoiceBackgroundResource);
            itemView.setOnClickListener(null);
        }
    }
```
>机智如我，啦啦啦....对于小猿大神我来说，都是小菜一碟。小猿得意的哼起了歌，得意得有点欠揍

###### 是时候解决产品提的那两个需求了：
展示头像倒是没难度的，关键是如何让搜索根据选择条目的数量动态改变宽度？能不能计算出当前RecyclerView占用的宽度呢？如果能知道的话事情不就解决了吗？嗯嗯，撸串代码测试一下：
```
private void refreshLayout(boolean isSelected) {
        mIvSelectAll.setSelected(isSelected);

        if (mAdapter.getSelectionList().size() == mIconListRvAdapter.getItemCount()) {
            mIvSelectAll.setSelected(true);
        }

        int size = mSelectList.size();
        mTvConfirm.setText(size == 0 ? "确定" : "确定(" + size + ")");
        if (size == 0) {
            mTvConfirm.setEnabled(false);
        } else {
            mTvConfirm.setEnabled(true);
        }

        int width = mItemWidth * size;
        if (width > mMaxWidth) {
            width = mMaxWidth;
        }
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, LinearLayout.LayoutParams.WRAP_CONTENT);
        mIconRecyclerView.setLayoutParams(params);
        mContainer.requestLayout();
    }
```
>完美、完美、完美、简直不要太完美。这里主要做三件事：
如果手动选择时，所有的条目都被选中，全选按钮也会变成选中状态;
设置确定按钮的选中条目的数量，并根据数量设置确定按钮是否可点击;
动态改变搜索栏的宽度
mItemWidth是怎么计算的？
```
  private void initData() {
        int screenWidth = ScreenUtils.getScreenWidth(mContext);
        mMaxWidth = screenWidth * 2 / 3;
        mItemWidth = SizeUtils.dp2px(mContext, 45);
        mWidth5 = SizeUtils.dp2px(mContext, 5);
    }
```
>你没看错，写死了，可惜不能插入捂脸的表情。RecyclerView一个item占用45dp，这个在写布局的时候就已经知道了。当然这种硬编码可不是个好习惯哦，不要学习。

###### 四、进化：
尝到甜头的小猿现在感觉自己无所不能，哎呀，这个效果我还是很不满意呀，为了展示逼格得加个动画不可。经过千辛万苦，各种尝试，总算有了能用的代码：
```
public void translationView(final View itemView, final Filter item) {
        getParentPoint();
        itemView.setClickable(false);
        final FloatImgBean floatImg = getFloatImg();
        floatImg.mImageView.setVisibility(View.VISIBLE);
        mPlaceHolder.setVisibility(View.VISIBLE);
        floatImg.mImageView.setImageResource(item.getImageResource());
        MultiSelecter.mImageLoader.showImage(mContext, item.getImageUrl(), floatImg.mImageView);
        floatImg.mIsAnimator = true;

        int[] sourceLocation = new int[2];
        mSourceView.getLocationOnScreen(sourceLocation);
        int startX = sourceLocation[0];
        int startY = sourceLocation[1];

        int[] tagetLocation = new int[2];
        mIconRecyclerView.getLocationOnScreen(tagetLocation);
        int endX = tagetLocation[0] + mIconRecyclerView.getWidth() + mWidth5;

        int endY = tagetLocation[1] + mWidth5 * 2;

        animator(itemView, item, floatImg, startX, startY, endX, endY);
    }

    private void animator(final View itemView, final Filter item, final FloatImgBean floatImg, int startX, int startY, int endX, int endY) {
        ObjectAnimator animatorX = ObjectAnimator.ofFloat(floatImg.mImageView, "translationX", startX - mStartX, endX - mStartX);
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(floatImg.mImageView, "translationY", startY - mStartY, endY - mStartY);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animatorX, animatorY);
        animatorSet.setDuration(calcDuration(startX - endX, startY - endY));
        animatorSet.setInterpolator(new OvershootInterpolator(1.1f));
        animatorSet.start();
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                floatImg.mImageView.setVisibility(View.GONE);
                floatImg.mIsAnimator = false;
                mIconListRvAdapter.add(item);
                mIconRecyclerView.smoothScrollToPosition(mIconListRvAdapter.getItemCount());
                mPlaceHolder.setVisibility(View.GONE);
                refreshLayout(false);
                itemView.setClickable(true);
            }
        });
    }
```
>这里小猿可是踩了无数坑，这里可以详细说下。敲黑板，这里可以高潮部分，就不要打瞌睡啦！

>首先是getLocationOnScreen 这个操蛋的方法，网上一致说这个获得的是当前view相当于整个屏的坐标位置，根据android坐标系的尿性，那屏幕最左上角就是原点咯，那我通过这个方法就是能获得顶部头像栏内条目的绝对坐标了咯，简直不要太爽。
然并卵，根本不是那么回事好吗？直到现在为止，小猿仍然没有搞懂这个方法获取的坐标会受到哪些因素的影响。但是可以肯定的是如果获取Item的坐标会出现偏移，貌似跟padding有关，有知道的可以在评论区解答。
但是获取mIconRecyclerView的坐标是没有问题的，这样我只要计算mIconRecyclerView的宽度就可以知道X坐标了。

>android的属性动画执行过程中，如果执行了另外的属性动画，会中断之前的执行，导致动画监听发生错乱。
整个逻辑全都乱套了，为此小猿想出了一个奇淫巧技，java不是有线程池么？为什么我不能弄一个ImageView的Pool,装上几个ImageView，没有在执行动画的取出来用，执行动画的让它自己玩去。于是有了下面这个：
```
private void initFloatPool() {
        FloatImgBean bean1 = new FloatImgBean();
        bean1.mImageView = mFloatImg_1;
        FloatImgBean bean2 = new FloatImgBean();
        bean2.mImageView = mFloatImg_2;
        FloatImgBean bean3 = new FloatImgBean();
        bean3.mImageView = mFloatImg_3;
        mImagePool.add(bean1);
        mImagePool.add(bean2);
        mImagePool.add(bean3);
    }

    private FloatImgBean getFloatImg() {
        for (FloatImgBean bean : mImagePool) {
            if (!bean.mIsAnimator) {
                return bean;
            }
        }
        return null;
    }
```
>当然这个用FloatImgBean 包装了一下，记录了当前ImageView是否有在执行动画。
为了让动画的执行更自然作出了一个牺牲，在mIconRecyclerView和搜索栏中间加了一个和mIconRecyclerView 的Item等宽的一个view,在动画执行的过程中设置成VISIBLE或者GONE来迎合动画的执行。
###### 五、示例：
完成了所有的功能，小猿瘫坐在电脑椅上，脸上露出满意的微笑。
忽然小猿虎躯一震，不行，我还得写个示例，不然怎么在人前装逼呢？
先实现一个Bean 类：
```
public class UserBean implements Filter {
    public String userName;
    public int icon;
    public int age;
    public boolean isSelected;
    public String iconUrl;

    @Override
    public boolean isSelected() {
        return isSelected;
    }

    @Override
    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    @Override
    public int getImageResource() {
        return icon;
    }

    @Override
    public String getImageUrl() {
        return iconUrl;
    }

    @Override
    public int filter() {
        if (age<3){
            return Filter.NO_CHOICE;
        }else if (age>=3&&age<100){
            return Filter.NORMAL;
        }
        return 0;
    }

    @Override
    public boolean isMatch(String condition) {
        if (MatchUtils.isMatch(userName,condition)){
            return true;
        }
        return false;
    }
}
```
如何调用那封装好的东西呢？
 ```
MultiSelectView selectView = new MultiSelecter.Builder(this, container)
                .setImageLoader(new GlideImageLoader())
                .setMultiAdapter(new UserAdapter())
                .setSelectType(MultiSelecter.MULTI_SELECT)
                .register(UserBean.class, R.layout.item_user)
                .register(TitleBean.class, R.layout.item_title)
                .build();
```
当然还得来一个ImageLoader的示例：
```
public class GlideImageLoader implements IImageLoader {
    @Override
    public void showImage(Context context, String url, ImageView imageView) {
        Glide.with(context)
                .load(url)
                .into(imageView);
    }
}
```
哦了，好累，小猿来了个葛优躺....

用到的开源项目：

**[AutoRecycleView](https://github.com/GaoGersy/AutoRecycleView)**：自动维护下拉刷新和上拉加载更多

**[SimpleMutiTypeAdapter](https://github.com/GaoGersy/SimpleMutiTypeAdapter)** ：使用起来很简单的RecyclerView多条目

没错，都是小猿之前为了应付需求改动写的。
