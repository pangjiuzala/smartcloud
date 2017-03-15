<?php if (!defined('THINK_PATH')) exit();?><html>
			<head>
						<link href="__PUBLIC__/images/skin.css" rel="stylesheet" type="text/css" />
						<meta http-equiv="Content-Type" content="text/html; charset=gb2312" /><style type="text/css">
						body {
							margin-left: 0px;
							margin-top: 0px;
							margin-right: 0px;
							margin-bottom: 0px;
							background-color: #EEF2FB;
						}
						</style>
			</head>
			<body>
			<table width="100%" border="0" cellpadding="0" cellspacing="0">
				  <!--第一行-->
					  <tr>
							<td width="17" valign="top" background="__PUBLIC__/images/mail_leftbg.gif">
									<img src="__PUBLIC__/images/left-top-right.gif" width="17" height="29" />
							</td>
							<td valign="top" background="__PUBLIC__/images/content-bg.gif">
									<table width="100%" height="31" border="0" cellpadding="0" cellspacing="0" class="left_topbg" id="table2">
											  <tr>
														<td height="31"><div class="titlebt">配置考勤</div></td>
											  </tr>
									</table>
							</td>
							<td width="16" valign="top" background="__PUBLIC__/images/mail_rightbg.gif">
										<img src="__PUBLIC__/images/nav-right-bg.gif" width="16" height="29" />
							</td>
					  </tr>
				  <!--第二行-->
					  <tr>
								<td valign="middle" background="__PUBLIC__/images/mail_leftbg.gif">&nbsp;</td>
								<td valign="top" bgcolor="#F7F8F9">
								<table width="98%" border="0" align="center" cellpadding="0" cellspacing="0">
										  <tr>
													<td colspan="2" valign="top">&nbsp;</td>
													<td>&nbsp;</td>
													<td valign="top">&nbsp;</td>
										  </tr>
										  <tr>
													<td width="7%">&nbsp;</td>
													<td valign="top" colspan="3">
															<table width="100%" height="144" border="0" cellpadding="0" cellspacing="0" class="line_table">
																	  <tr>
																				<td width="93%" bgcolor="#D3DCE3" class="left_bt2" align="center">配置考勤
																				</td>
																	  </tr>
																	  <tr>
																				<td height="102" valign="top" colspan="3">
																				<form action="__URL__/do_exam" method="post">
																				<table border="0" width="100%">
																						<tr>
																								<td align="right" width="45%" bgcolor="#E5E5E5">考勤序号：</td>
																								<td bgcolor="#E5E5E5"><input type="text" name="exam_record_id">&nbsp;<font color="blue" size="2px"><b>上次是第<?php echo ($last_exam_record_id); ?>次考勤</b></font></td>
																						</tr>
																						<tr>
																								<td align="right" bgcolor="#D5D5D5">正常开始时间：</td>
																								<td bgcolor="#D5D5D5">
																										<?php echo "<select name='ontime_start_hour'>"; for($i=6;$i<=23;$i++){ echo "<option value='$i'>$i</option>"; } echo "</select>"; ?>&nbsp;时
																										<?php echo "<select name='ontime_start_minute'>"; for($i=0;$i<=60;$i++){ echo "<option value='$i'>$i</option>"; } echo "</select>"; ?>&nbsp;分
																								</td>
																						</tr>
																						<tr>
																								<td align="right" bgcolor="#E5E5E5">正常结束时间：</td>
																								<td bgcolor="#E5E5E5">
																									<?php echo "<select name='ontime_end_hour'>"; for($i=6;$i<=23;$i++){ echo "<option value='$i'>$i</option>"; } echo "</select>"; ?>&nbsp;时
																									<?php echo "<select name='ontime_end_minute'>"; for($i=0;$i<=60;$i++){ echo "<option value='$i'>$i</option>"; } echo "</select>"; ?>&nbsp;分
																								</td>
																						</tr>
																						<tr>
																								<td align="right" bgcolor="#D5D5D5">迟到开始时间：</td>
																								<td bgcolor="#D5D5D5">
																										<?php echo "<select name='late_start_hour'>"; for($i=6;$i<=23;$i++){ echo "<option value='$i'>$i</option>"; } echo "</select>"; ?>&nbsp;时
																									<?php echo "<select name='late_start_minute'>"; for($i=0;$i<=60;$i++){ echo "<option value='$i'>$i</option>"; } echo "</select>"; ?>&nbsp;分
																								</td>
																						</tr>
																						<tr>
																								<td align="right" bgcolor="#E5E5E5">迟到结束时间：</td>
																								<td bgcolor="#E5E5E5">
																											<?php echo "<select name='late_end_hour'>"; for($i=6;$i<=23;$i++){ echo "<option value='$i'>$i</option>"; } echo "</select>"; ?>&nbsp;时
																									<?php echo "<select name='late_end_minute'>"; for($i=0;$i<=60;$i++){ echo "<option value='$i'>$i</option>"; } echo "</select>"; ?>&nbsp;分
																								</td>
																						</tr>
																						<tr>
																								<td colspan="2" align="center" bgcolor="#D5D5D5">
																										<input type="submit" name="submit" value="配置">
																										<input type="reset" name="reset" value="重置">
																								</td>
																						</tr>
																				</table>
																				</form>
																				</td>
																			
																	  </tr>
																	  <tr>
																				<td colspan="3">&nbsp;</td>
																	  </tr>
															</table>
													</td>
													<td width="7%">&nbsp;</td>
										  </tr>
										  <tr>
													<td colspan="2" valign="top">
													</td>
													<td>&nbsp;</td>
													<td valign="top"></td>
										  </tr>
								  <tr>
											<td height="40" colspan="4"></td>
								  </tr>
								</table>
						</td>
						<td background="__PUBLIC__/images/mail_rightbg.gif">&nbsp;</td>
					  </tr>
					 <!--第三行-->
					  <tr>
								<td valign="bottom" background="__PUBLIC__/images/mail_leftbg.gif">
											<img src="__PUBLIC__/images/buttom_left2.gif" width="17" height="17" />
								</td>
								<td background="__PUBLIC__/images/buttom_bgs.gif">
											<img src="__PUBLIC__/images/buttom_bgs.gif" width="17" height="17">
								</td>
								<td valign="bottom" background="__PUBLIC__/images/mail_rightbg.gif">
											<img src="__PUBLIC__/images/buttom_right2.gif" width="16" height="17" />
								</td>
					  </tr>
			</table>
			</body>
</html>