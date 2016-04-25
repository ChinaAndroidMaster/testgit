package com.creditease.checkcars.tools;

import android.content.Context;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

import com.creditease.checkcars.config.Config;

/**
 * Share分享辅助类
 *
 * @author zgb
 */
public class ShareUtils
{
    private static ShareUtils utils;

    public static ShareUtils getUtils(Context context)
    {
        if ( utils == null )
        {
            utils = new ShareUtils();
        }
        utils.mContext = context;
        return utils;
    }

    private Context mContext;

    private ShareUtils()
    {
        super();
    }

    /**
     * share一键分享
     */
    public void showShare(String imageUrl, String uuid, String text, String title)
    {
        String sharePath = Config.WeiXin_APPRAISER_HOMEPAGE_URL + uuid;
        ShareSDK.initSDK(mContext);
        OnekeyShare oks = new OnekeyShare();
        // 关闭sso授权
        oks.disableSSOWhenAuthorize();
        // 分享时Notification的图标和文字 2.5.9以后的版本不调用此方法
        // oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(title);
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl(sharePath);
        // text是分享文本，所有平台都需要这个字段
        oks.setText(text);
        // 保存网络路径的图片资源与setImagePath二选一
        oks.setImageUrl(imageUrl);
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(sharePath);
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment(text);
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(title);
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl(sharePath);
        // 启动分享GUI
        oks.show(mContext);
    }
}
