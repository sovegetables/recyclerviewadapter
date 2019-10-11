#### Setup
```gradle
compile 'com.albert.recyclerview:lazyadapter:0.1.0'
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




