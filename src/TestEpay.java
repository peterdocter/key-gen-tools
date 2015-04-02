import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by liyong on 3/19/15.
 */
public class TestEpay {
    public static void main(String[] args) throws UnsupportedEncodingException {
        String baseUrl = "https://epay.163.com/addMobileTradeServlet?";
        String sellerId = "htdistribution@wyb.163.com";
        String sellerNickName = "huihui";
        String buyerNickName = "buyer_test";
        String goodsName = "苹果笔记本";
        String goodsUrl = "www.huihui.cn";
        String tradeAmount = "159";
        String platformId = "2014121521PT33527535";
        String num = "1";
        String platformTradeId = "679";
        String tradeType = "1";
        String logisticsType = "0";
        String platformTradeTime = "2015-03-19 14:57:00";
        String notifyUrl = "http://www.huihui.cn";
        String payAccountId = "huihui_test1991@163.com";
        String paymethod = "6001";
        String signature = "";
        String noticeType = "server";
        String beforeSign = sellerId + tradeAmount + logisticsType
                + platformId + platformTradeId + platformTradeTime
                + notifyUrl + "" + "" + payAccountId ;
        String epayUrl =  baseUrl;
        epayUrl += "sellerId=" + sellerId;
        epayUrl += "&sellerNickName=" + sellerNickName;
        epayUrl += "&buyerNickName=" + buyerNickName;
        epayUrl += "&goodsName=" + goodsName;
        epayUrl += "&goodsUrl=" + goodsUrl;
        epayUrl += "&tradeAmount=" + tradeAmount;
        epayUrl += "&platformId=" + platformId;
        epayUrl += "&num=" + num;
        epayUrl += "&platformTradeId=" + platformTradeId;
        epayUrl += "&tradeType=" + tradeType;
        epayUrl += "&logisticsType=" + logisticsType;
        epayUrl += "&platformTradeTime=" + platformTradeTime;
        epayUrl += "&notifyUrl=" + notifyUrl;
        epayUrl += "&payAccountId=" + payAccountId;
        epayUrl += "&noticeType=" + noticeType;
        epayUrl += "&signature=" + NetEaseSignUtil.generateSHA1withRSASigature(NetEaseSignUtil.priKey_Hex_Str, URLEncoder.encode(beforeSign,"UTF-8"));
        System.out.println(epayUrl);
        String result = HttpUtil.sendGet(epayUrl);
        System.out.println("result:" + result);

    }
}
