package lyh.rookiemall.adapter;

import android.content.Context;

import java.util.List;

import lyh.rookiemall.R;
import lyh.rookiemall.bean.Category;

/**
 * Created by 刘营海 on 2017/8/8.
 */

public class CategoryAdapter extends SimpleAdapter<Category> {
    public CategoryAdapter(Context context, List<Category> datas) {
        super(context, R.layout.template_single_text, datas);
    }
    @Override
    protected void convert(BaseViewHolder viewHoder, Category item) {
        viewHoder.getTextView(R.id.textView).setText(item.getName());

    }
}
