package org.wordpress.android.ui.posts;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;

import org.wordpress.android.R;
import org.wordpress.android.models.Post;
import org.wordpress.android.util.StringUtils;
import org.wordpress.android.util.WPHtml;

/**
 * Created by dan on 11/26/13.
 */
public class EditPostPreviewFragment extends SherlockFragment {

    NewEditPostActivity mActivity;
    WebView mWebView;
    TextView mTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mActivity = (NewEditPostActivity)getActivity();

        ViewGroup rootView = (ViewGroup) inflater
                .inflate(R.layout.fragment_edit_post_preview, container, false);
        mWebView = (WebView) rootView.findViewById(R.id.post_preview_webview);
        mTextView = (TextView) rootView.findViewById(R.id.post_preview_textview);

        return rootView;
    }

    public void loadPost(Post post) {

        // Don't load if the Post object of title are null, see #395
        if (post == null || post.getTitle() == null)
            return;

        String postTitle = "<h1>" + post.getTitle() + "</h1>";
        String postContent = postTitle + post.getDescription() + "\n\n" + post.getMt_text_more();

        if (post.isLocalDraft()) {
            mTextView.setVisibility(View.VISIBLE);
            mWebView.setVisibility(View.GONE);
            mTextView.setText(WPHtml.fromHtml(postContent.replaceAll("\uFFFC", ""), getActivity().getBaseContext(), post));
        } else {
            mTextView.setVisibility(View.GONE);
            mWebView.setVisibility(View.VISIBLE);

            String htmlText = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?><html><head><link rel=\"stylesheet\" type=\"text/css\" href=\"webview.css\" /></head><body><div id=\"container\">%s</div></body></html>";
            htmlText = String.format(htmlText, StringUtils.addPTags(postContent));
            mWebView.loadDataWithBaseURL("file:///android_asset/", htmlText,
                    "text/html", "utf-8", null);
        }

    }
}
