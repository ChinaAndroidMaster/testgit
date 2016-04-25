package com.creditease.checkcars.net.oper;

import android.content.Context;
import android.os.Bundle;

import com.creditease.checkcars.config.Config;
import com.creditease.checkcars.data.db.MineTopicUtil;
import com.creditease.checkcars.data.encrypt.DESEncrypt;
import com.creditease.checkcars.data.sp.manager.SharePrefenceManager;
import com.creditease.checkcars.exception.CEDbException;
import com.creditease.checkcars.exception.CEFailtureException;
import com.creditease.checkcars.exception.CERequestException;
import com.creditease.checkcars.net.factory.OperResponseFactory;
import com.creditease.checkcars.net.oper.base.Oper;
import com.creditease.checkcars.net.oper.base.OperManager.RequestParam;
import com.creditease.checkcars.net.oper.base.OperResponse;
import com.creditease.checkcars.net.oper.bean.MineTopicsBean.MineTopicObj;
import com.creditease.checkcars.net.oper.bean.MineTopicsBean.MineTopicResult;
import com.creditease.checkcars.tools.JsonUtils;

public class GetMineTopicsOperation extends Oper< Object >
{

    private String uuid;
    private int operId;// 0 我的主页请求服务评价 1 服务评价页请求服务评价 通过此参数保存不同的时间值

    public GetMineTopicsOperation(Context mContext, RequestListener listener)
    {
        super(mContext, listener);
    }

    public GetMineTopicsOperation(Context mContext, RequestListener listener, int operId)
    {
        this(mContext, listener);
        this.operId = operId;
    }

    @Override
    public Bundle dataParseAndHandle(String result) throws CERequestException, CEFailtureException
    {
        OperResponse res = OperResponseFactory.parseResult(result);
        String data = DESEncrypt.decryption(res.data);
        MineTopicResult obj = JsonUtils.parserJsonStringToObject(data, MineTopicResult.class);
        if ( obj.dataList != null )
        {
            try
            {
                if ( obj.dataList.size() > 0 )
                {
                    if ( operId == 0 )
                    {
                        SharePrefenceManager.setMainTopicTime(mContext,
                                String.valueOf(obj.dataList.get(0).modifyTime), uuid);
                    } else if ( operId == 1 )
                    {
                        SharePrefenceManager.setTopicTime(mContext,
                                String.valueOf(obj.dataList.get(0).modifyTime), uuid);
                    }
                }
                MineTopicUtil.getUtil(mContext).saveOrUpdateTopics(obj.dataList, uuid);
            } catch ( CEDbException e )
            {
                e.printStackTrace();
            }
        }
        Bundle bundle = new Bundle();
        bundle.putParcelable(BUNDLE_EXTRA_RESPONSE, res);
        bundle.putParcelableArrayList(BUNDLE_EXTRA_DATA, obj.dataList);
        bundle.putString("userdata", "topics");
        return bundle;
    }

    @Override
    protected String getUrl()
    {
        return Config.WS_MINE_TOPIC_LIST;
    }

    public void setParam(int curPage, int pageSize, String uuid, long modifyTime, int operType)
    {
        this.uuid = uuid;
        String json =
                JsonUtils
                        .requestObjectBean(new MineTopicObj(curPage, pageSize, uuid, modifyTime, operType));
        String topicData = DESEncrypt.encryption(json);
        RequestParam param = new RequestParam();
        param.addBodyParameter("para", topicData);
        setParam(param);
    }
}
