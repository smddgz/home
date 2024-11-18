package org.example.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 *  {
 *             "instType": "SPOT",
 *             "instId": "BTC-USDC",
 *             "last": "76506.7",
 *             "lastSz": "0.00007842",
 *             "askPx": "76512",
 *             "askSz": "0.0264",
 *             "bidPx": "76511.9",
 *             "bidSz": "0.09334765",
 *             "open24h": "76171.5",
 *             "high24h": "77265.6",
 *             "low24h": "75630.4",
 *             "volCcy24h": "75165762.311285472",
 *             "vol24h": "982.55937683",
 *             "ts": "1731149071314",
 *             "sodUtc0": "76564",
 *             "sodUtc8": "76496"
 *         }
 */
@Data
@TableName("crypto_info")
public class CryptoInfo {
    String instId;
    String last;
    String createdDate;
}
