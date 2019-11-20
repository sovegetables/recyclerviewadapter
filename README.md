[![](https://jitpack.io/v/sovegetables/recyclerviewadapter.svg)](https://jitpack.io/#sovegetables/recyclerviewadapter)

## LazyAdapter
#### Setup
```gradle
maven { url "https://jitpack.io" }

implementation 'com.github.sovegetables.recyclerviewadapter:lazyadapter:0.1.1'

```
#### Usage

1. HeaderAndFooterListRecyclerAdapter
```sh
 private class Adapter extends HeaderAndFooterListRecyclerAdapter<String>{
    public Adapter(Context context, List<String> data) {
        super(context, data);
    }

    @Override
    protected int getItemLayoutRes() {
        return R.layout.mulit_choice_item;
    }

    @Override
    protected void onBindDataViewHolder(XOLazyRecyclerViewHolder holder, String item, int dataIndex, int position) {
        TextView tv = holder.get(R.id.tv_item);
        tv.setText(item);
    }
}

mRv.setAdapter(mAdapter);
mAdapter.setData(data)

mAdapter.addHeader(layoutId, onBindHeaderViewListener)
mAdapter.addFooter(layoutId, onBindFooterViewListener)
```
2. MultiChoiceListRecyclerAdapter
```sh
private class Adapter extends MultiChoiceListRecyclerAdapter<String>{

        public Adapter(Context context) {
            super(context);
        }

        @Override
        protected int getChoiceMode() {
            return CHOICE_MODE_MULTIPLE_MODAL;
        }

        @Override
        protected void onBindMultiChoiceViewHolder(XOLazyRecyclerViewHolder holder, String item, int dataIndex, int position) {
            TextView tv = holder.get(R.id.tv_item);
            tv.setText(item);
            CheckBox cb = holder.get(R.id.cb);
            final SparseBooleanArray selectedMap = getSelectStates();
            if(selectedMap != null && selectedMap.get(position)){
                cb.setChecked(true);
            }else {
                cb.setChecked(false);
            }
        }

        @Override
        protected int getItemLayoutRes() {
            return R.layout.mulit_choice_item;
        }
    }

rv.setAdapter(mAdapter);
mAdapter.setMultiChoiceModeListener(new MultiChoiceListRecyclerAdapter.MultiChoiceModeListener() {

            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, boolean checked) {
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
            }
        });
rv.setData(data);
```
3.LoadMoreListRecyclerAdapter
```sh
private class Adapter extends LoadMoreListRecyclerAdapter<String> {
    public Adapter(Context context) {
        super(context);
    }

    @Override
    protected int getItemLayoutRes() {
        return R.layout.mulit_choice_item;
    }

    @Override
    protected void onBindDataViewHolder(XOLazyRecyclerViewHolder holder, String item, int dataIndex, int position) {
    }
}

mAdapter = new Adapter(this);
mAdapter.setLoadMoreLayout(R.layout.spinner);
mAdapter.setOnLoadMoreListener(new OnLoadMoreListener(){
    public void onLoadMore(LoadMoreListRecyclerAdapter wrapperAdapters){
        //load more
    }
});
mRv.setAdapter(mAdapter);
mAdapter.setData(new ArrayList<>(Arrays.asList(Data.ITEMS)));
```
4.OnItemClickListener

mAdapter.setOnItemClickListener(new OnItemClickListener<Item>(){

    public void onItemClick(View view, Item item, int position){

    }
});

5.Multi View Type
```sh
public abstract class MultiAdapter<T> extends BaseRecyclerAdapter<List<T>, T>{

    @Override
    protected void buildRecyclerViewType(RecyclerViewTypeBuilder builder) {
        builder.addRecyclerViewType(new ViewType1(this))
                .addRecyclerViewType(new ViewType2(this))
                .addRecyclerViewType(new ViewType3(this));
    }

    private static class ViewType1 extends RecyclerViewType {

            public DataRecyclerViewType(MultiAdapter wrapperAdapters){
                super(wrapperAdapters);
            }

            @Override
            protected boolean isMatchViewType(int position) {
                return true;
            }

            @Override
            protected int getItemViewType() {
                return 0;
            }

            @Override
            protected int getItemLayoutRes() {
                return getAdapter().getItemLayoutRes();
            }

            protected void onBindViewHolder(XOLazyRecyclerViewHolder holder, Object item, int position) {

            }
        }
}
```

#### Feature
- Multi View Type
- Load More
- Long press enter choice mode: multi choice, single choice
- Header and Footer

## CommonAdapter
#### Setup
```gradle
maven { url "https://jitpack.io" }

implementation 'com.github.sovegetables.recyclerviewadapter:adapter:0.1.1'

```
#### Usage
- 单类型
```kotlin
adapter = Adapter()
adapter.setOnItemClickListener { _, item, _ ->
    
}
adapter.items = categories
recyclerview.adapter = adapter

private class Adapter : AbsListAdapter<String>() {

    override fun onBindView(holder: CommonViewHolder, s: String, position: Int) {
        val btn = holder.findViewById<TextView>(R.id.btn_category)
        btn.text = s
    }
    
    override fun getLayoutRes(): Int {
        return R.layout.item_categories
    }
}
```
- 支持多类型
```kotlin
val adapter = InternalAdapter()
val list = arrayListOf<Item>()
list.add(object : Item{
    override fun type(): Item.TYPE {
        return Item.TYPE.THREE
    }
})
list.add(object : Item{
    override fun type(): Item.TYPE {
        return Item.TYPE.ONE
    }
})
list.add(object : Item{
    override fun type(): Item.TYPE {
        return Item.TYPE.TWO
    }
})
adapter.items = list
recyclerview.adapter = adapter

private class InternalAdapter: AbsDelegationAdapter<List<Item>>(){

    interface OnActionListener{
        fun onAction1(item: Item)
        fun onAction2(item: Item)
        fun onAction3(item: Item)
    }

    private var listener : OnActionListener? = null

    fun setOnActionListener(l: OnActionListener?){
        listener = l
    }

    init {
        delegatesManager.addDelegate(object : ListAdapterDelegate<Item>(){
            override fun onBindView(holder: LazyRecyclerViewHolder?, t: Item?, position: Int) {
            }

            override fun isForViewType(items: MutableList<Item>, position: Int): Boolean {
                return items[position].type() == Item.TYPE.ONE
            }

            override fun getLayoutRes(): Int {
                return R.layout.item_mutli_type_one
            }

        })

        delegatesManager.addDelegate(object : ListAdapterDelegate<Item>(){
            override fun onBindView(holder: LazyRecyclerViewHolder?, t: Item?, position: Int) {
            }

            override fun isForViewType(items: MutableList<Item>, position: Int): Boolean {
                return items[position].type() == Item.TYPE.TWO
            }

            override fun getLayoutRes(): Int {
                return R.layout.item_mutli_type_two
            }

        })

        delegatesManager.addDelegate(object : ListAdapterDelegate<Item>(){

            override fun onViewCreated(parent: ViewGroup?, holder: LazyRecyclerViewHolder) {
                super.onViewCreated(parent, holder)

                val listener = View.OnClickListener {
                    when(it.id){
                        R.id.btn_one ->{
                            listener?.onAction1(AdapterDelegate.getItemByTag(holder))
                        }
                        R.id.btn_three ->{
                            listener?.onAction3(AdapterDelegate.getItemByTag(holder))
                        }
                    }
                }
                holder.findViewById<Button>(R.id.btn_one).setOnClickListener(listener)
                holder.findViewById<Button>(R.id.btn_two).setOnClickListener(listener)
            }

            override fun onBindView(holder: LazyRecyclerViewHolder?, t: Item?, position: Int) {
                AdapterDelegate.setItemTag(holder, t)
                AdapterDelegate.setPositionTag(holder, position)
            }

            override fun isForViewType(items: MutableList<Item>, position: Int): Boolean {
                return items[position].type() == Item.TYPE.THREE
            }

            override fun getLayoutRes(): Int {
                return R.layout.item_mutli_type_three
            }

        })
    }

    override fun getItemCount(): Int {
        return items?.size?: 0
    }

}
```


#### Feature




