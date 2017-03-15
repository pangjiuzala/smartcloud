/**
 * 
 */
package com.nuc.smartcloud.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

public class SmsReceiver extends BroadcastReceiver {
	// �����յ�����ʱ������
	@Override
	public void onReceive(Context context, Intent intent) {
		// ����ǽ��յ�����
		if (intent.getAction()
				.equals("android.provider.Telephony.SMS_RECEIVED")) {
			// ȡ���㲥�����д��뽫����ϵͳ�ղ������ţ�
			abortBroadcast();
			StringBuilder sb = new StringBuilder();
			// ������SMS������������
			Bundle bundle = intent.getExtras();
			// �ж��Ƿ�������
			if (bundle != null) {
				// ͨ��pdus���Ի�ý��յ������ж�����Ϣ
				Object[] pdus = (Object[]) bundle.get("pdus");
				// �������Ŷ���array,�������յ��Ķ��󳤶�������array�Ĵ�С
				SmsMessage[] messages = new SmsMessage[pdus.length];
				for (int i = 0; i < pdus.length; i++) {
					messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
				}
				// �������Ķ��źϲ��Զ�����Ϣ��StringBuilder����
				for (SmsMessage message : messages) {
					sb.append("������Դ:");
					// ��ý��ն��ŵĵ绰����
					sb.append(message.getDisplayOriginatingAddress());
					sb.append("\n------��������------\n");
					// ��ö��ŵ�����
					sb.append(message.getDisplayMessageBody());
				}
			}
			Toast.makeText(context, sb.toString(), 5000).show();
		}
	}
}
