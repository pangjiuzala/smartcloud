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
														<td height="31"><div class="titlebt">会员界面</div></td>
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
																				<td  height="27" background="__PUBLIC__/images/main_07.gif">
																							<img src="__PUBLIC__/images/main_07.gif" width="2" height="27">
																				</td>
																				<td width="93%" background="__PUBLIC__/images/main_07.gif" class="left_bt2" align="center">添加会员
																				</td>
																				<td  height="27" background="__PUBLIC__/images/main_07.gif">
																							<img src="__PUBLIC__/images/main_07.gif" width="2" height="27">
																				</td>
																	  </tr>
																	  <tr>
																				<td height="102" valign="top" colspan="3">
																				<form action="__URL__/do_mem" method="post">
																				<table border="0" width="100%">
																						<tr>
																								<td align="right" width="45%" bgcolor="#CCCCCC">学号：</td>
																								<td bgcolor="#CCCCCC"><input type="text" name="member_id"></td>
																						</tr>
																						<tr>
																								<td align="right">姓名：</td>
																								<td><input type="text" name="member_name"></td>
																						</tr>
																						<tr>
																								<td align="right" bgcolor="#CCCCCC">联系方式：</td>
																								<td bgcolor="#CCCCCC"><input type="text" name="member_phone"></td>
																						</tr>
																						<tr>
																								<td align="right">学院：</td>
																								<td>
																								<select name="member_college">
																									<option value="1">软件学院</option>
																									<option value="2">非软件学院</option>
																									<option value="3">研究生</option>
																									<option value="4">教师</option>
																								</select> 
																								</td>
																						</tr>
																						<tr>
																								<td colspan="2" align="center" bgcolor="#CCCCCC">
																										<input type="submit" name="submit" value="submit">
																										<input type="reset" name="reset" value="reset">
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