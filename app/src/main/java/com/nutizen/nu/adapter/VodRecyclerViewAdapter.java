package com.nutizen.nu.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.nutizen.nu.R;
import com.nutizen.nu.bean.response.CommentResult;
import com.nutizen.nu.bean.response.ContentResponseBean;
import com.nutizen.nu.bean.response.LoginResponseBean;
import com.nutizen.nu.common.MyApplication;
import com.nutizen.nu.dialog.NormalDialog;
import com.nutizen.nu.presenter.LoginPresenter;
import com.nutizen.nu.utils.DownArrowAnimUtil;
import com.nutizen.nu.utils.ScreenUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 * Created by Administrator on 2017/3/17.
 */

public class VodRecyclerViewAdapter extends RecyclerView.Adapter implements View.OnClickListener {

    final private int DEFAULT = 0;
    final private int LARGE = 2;
    final private int LARGE_UNWIND = 3;
    private Context mContext;
    private final int HEAD_VIEW_TYPE = 1;
    private final int END_VIEW_TYPE = 2;
    private boolean showEnd = true;
    private int userVid;
    private ContentResponseBean.SearchBean mVideoInfo;
    private String mWritter;
    private String mViewerNumber = "0";
    private ArrayList<CommentResult> mCommentResultList = new ArrayList<>();
    private CommentAdapterCallback mCommentAdapterCallback;
    private EditText commentEditText;
    private TextView showMoreTextView;
    private TextView mDescription;
    private TreeMap<Integer, Integer> unwindMap = new TreeMap<>();
    private int mAvailableWidth;
    private Typeface mHeavyTypeface, mBookTypeface;
    public int showDeletePosition = -1;

    public VodRecyclerViewAdapter(Context context, ContentResponseBean.SearchBean videoInfo) {
        LoginResponseBean accountMessage = LoginPresenter.getAccountMessage();
        if (accountMessage != null) {
            userVid = Integer.valueOf(accountMessage.getViewer_id());
        }
        mVideoInfo = videoInfo;
    }

    public void initAvailableWidth(int availableWidth) {
        if (mAvailableWidth != availableWidth)
            mAvailableWidth = availableWidth;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        mHeavyTypeface = Typeface.createFromAsset(mContext.getAssets(), "font/avenir/avenirHeavy.otf");
        mBookTypeface = Typeface.createFromAsset(mContext.getAssets(), "font/avenir/avenirLTStd_Book.ttf");
        if (viewType == HEAD_VIEW_TYPE) {
            View inflate = LayoutInflater.from(mContext).inflate(R.layout.item_vodplayer_firstitem, parent, false);
            return new HeadHolder(inflate);
        } else if (viewType == END_VIEW_TYPE) {
            View inflate = LayoutInflater.from(mContext).inflate(R.layout.item_vodplayer_lastitem, parent, false);
            return new EndHolder(inflate);
        } else {
            View inflate = LayoutInflater.from(mContext).inflate(R.layout.item_play_comments, parent, false);
            return new CommentHolder(inflate);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof HeadHolder) {
            final HeadHolder _holder = (HeadHolder) holder;
            mDescription = _holder.mDescription;
            commentEditText = _holder.mCommentEditText;
            _holder.mTitle.setText(mVideoInfo.getTitle());
            if (!mViewerNumber.equalsIgnoreCase("0")) {
                _holder.mViews.setVisibility(View.VISIBLE);
                _holder.mViews.setText(mContext.getString(R.string.viewers, mViewerNumber));
            }
            _holder.mDescription.setText(mVideoInfo.getDescription());
            _holder.mShowDescription.setOnClickListener(this);
            if (mWritter != null) {
                _holder.mWritter.setText(mWritter);
            }
            _holder.mSendIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String comment = _holder.mCommentEditText.getText().toString();
                    mCommentAdapterCallback.commitComment(comment);
                }
            });
        } else if (holder instanceof EndHolder) {
            EndHolder _holder = (EndHolder) holder;
            showMoreTextView = _holder.mShowMore;
            if (!showEnd) {
                _holder.mShowMore.setVisibility(View.GONE);
            }
            _holder.mShowMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCommentAdapterCallback.showMore();
                }
            });
        } else if (holder instanceof CommentHolder) {
            final CommentHolder _holder = (CommentHolder) holder;

            CommentResult.UserBean user = mCommentResultList.get(position - 1).getUser();
            _holder.mName.setText(user.getCommentNameShow());

            if (user.getViewer_id() == userVid) {
                _holder.mIvMore.setVisibility(View.VISIBLE);
            } else {
                _holder.mIvMore.setVisibility(View.GONE);
            }

            TextView content = _holder.mContent;
            String httpComment = mCommentResultList.get(position - 1).getComment().replace("\r", " ").replace("\n", " ");
            Spanned spanned = Html.fromHtml(httpComment);
            content.setText(spanned.toString());
            initUnwindText(content, _holder.mUnwind, position);

            if (position == showDeletePosition) {
                _holder.mTvDelete.setVisibility(View.VISIBLE);
            } else {
                _holder.mTvDelete.setVisibility(View.GONE);
            }

            _holder.mIvMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (_holder.mTvDelete.getVisibility() == View.VISIBLE) {
                        showDeletePosition = -1;
                    } else {
                        showDeletePosition = position;
                    }
                    notifyDataSetChanged();
                }
            });

            _holder.mTvDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDeletePosition = -1;
                    _holder.mTvDelete.setVisibility(View.GONE);
                    showDailog(position);
                }
            });
        }
    }

    private void showDailog(final int position) {
        new NormalDialog(mContext, mContext.getString(R.string.yes), mContext.getString(R.string.cancel), "Confirm Delete ?", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCommentAdapterCallback != null) {
                    mCommentAdapterCallback.longClickToDelete(mCommentResultList.get(position - 1));
                }
            }
        }, null).show();
    }

    private void initUnwindText(final TextView content, final ImageView unwind, final int position) {
        unwind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unwind.setVisibility(View.GONE);
                content.setMaxLines(Integer.MAX_VALUE);
                unwindMap.put(position, LARGE_UNWIND);
            }
        });
        //判断属于哪种类型
        Integer unwindType = unwindMap.get(position) != null ? unwindMap.get(position) : DEFAULT;
        switch (unwindType) {
            case DEFAULT:
                unwind.setVisibility(View.GONE);
                content.setMaxLines(2);
                break;
            case LARGE:
                unwind.setVisibility(View.VISIBLE);
                content.setMaxLines(2);
                break;
            case LARGE_UNWIND:
                unwind.setVisibility(View.GONE);
                content.setMaxLines(Integer.MAX_VALUE);
                break;
        }

        //每个测量判断是属于那种类型
        if (unwindMap.get(position) == null) {
            if (isOverFlowed(content)) {
                unwindMap.put(position, LARGE);
                unwind.setVisibility(View.VISIBLE);
                content.setMaxLines(2);
            } else {
                unwindMap.put(position, DEFAULT);
                unwind.setVisibility(View.GONE);
                content.setMaxLines(2);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mCommentResultList.size() + 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return HEAD_VIEW_TYPE;
        } else if (position == mCommentResultList.size() + 1) {
            return END_VIEW_TYPE;
        } else {
            return super.getItemViewType(position);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_show_detail:
                DownArrowAnimUtil.switchDownArrow(v, mDescription);
                break;
        }
    }

    /**
     * Holder  两个
     */

    private class HeadHolder extends RecyclerView.ViewHolder {

        private final ImageView mShowDescription;
        private final TextView mTitle;
        private final TextView mWritter;
        private final TextView mViews;
        private final TextView mDescription;


        private final EditText mCommentEditText;
        private final ImageView mSendIv;


        private HeadHolder(View itemView) {
            super(itemView);
            mShowDescription = itemView.findViewById(R.id.iv_show_detail);
            mTitle = itemView.findViewById(R.id.vod_title);
            mWritter = itemView.findViewById(R.id.tv_writter);
            mViews = itemView.findViewById(R.id.tv_views);
            mDescription = itemView.findViewById(R.id.tv_detail);
            mCommentEditText = itemView.findViewById(R.id.comment_et);
            mSendIv = itemView.findViewById(R.id.comment_send_iv);
            mDescription.setTypeface(mBookTypeface);
        }
    }

    private class EndHolder extends RecyclerView.ViewHolder {

        private final TextView mShowMore;

        private EndHolder(View itemView) {
            super(itemView);
            mShowMore = itemView.findViewById(R.id.comment_showmore);
            mShowMore.setTypeface(mHeavyTypeface);
        }
    }

    private class CommentHolder extends RecyclerView.ViewHolder {

        private final TextView mName;
        private final TextView mContent;
        private final ImageView mUnwind;
        private final ImageView mIvMore;
        private final TextView mTvDelete;

        private CommentHolder(View itemView) {
            super(itemView);
            mName = itemView.findViewById(R.id.comment_name);
            mContent = itemView.findViewById(R.id.comment_content);
            mUnwind = itemView.findViewById(R.id.comment_unfold);
            mIvMore = itemView.findViewById(R.id.iv_more);
            mTvDelete = itemView.findViewById(R.id.tv_delete);
            mName.setTypeface(mHeavyTypeface);
            mContent.setTypeface(mBookTypeface);
        }
    }

    /**
     * 数据绑定
     *
     * @param commentResultList
     */
    public void setCommentData(List<CommentResult> commentResultList) {
        mCommentResultList = (ArrayList<CommentResult>) commentResultList;
        resetEditText();
        notifyDataSetChanged();
    }

    public void setWritter(String writter) {
        mWritter = writter;
        notifyItemChanged(0);
    }

    public void setViewerNumber(int number) {
        DecimalFormat df = new DecimalFormat("###,###");
        mViewerNumber = df.format(number);
        notifyItemChanged(0);
    }

    public void setListener(CommentAdapterCallback callback) {
        mCommentAdapterCallback = callback;
    }

    public interface CommentAdapterCallback {
        void commitComment(String commit);

        void showMore();

        void longClickToDelete(CommentResult comment);
    }

    /**
     * 测量TextView是否超过两行
     *
     * @param tv
     * @return
     */
    private boolean isOverFlowed(TextView tv) {
        int availableWidth = (int) ((mAvailableWidth - ScreenUtils.dip2px(MyApplication.getMyApplicationContext(), 32)) * 2);
        Paint textViewPaint = tv.getPaint();
        float textWidth = (textViewPaint.measureText(tv.getText().toString()));
        return textWidth > availableWidth;
    }

    /**
     * 外部数据发生变化调用的方法
     */
    private void resetEditText() {
        if (commentEditText != null) {
            unwindMap.clear();
            commentEditText.setText("");
            commentEditText.setHint("");
            InputMethodManager imm = (InputMethodManager) MyApplication.getMyApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(commentEditText.getWindowToken(), 0);
        }
    }

    public void hideShowMore(boolean hide) {
        if (showMoreTextView != null) {
            if (hide) {
                showEnd = false;
                showMoreTextView.setVisibility(View.INVISIBLE);
            } else {
                showEnd = true;
                showMoreTextView.setVisibility(View.VISIBLE);
            }
        }
    }
}
