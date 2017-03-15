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
														<td height="31"><div class="titlebt">搜索会员</div></td>
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
								<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
										  <tr>
													<td colspan="2" valign="top">&nbsp;</td>
													<td>&nbsp;</td>
													<td valign="top">&nbsp;</td>
										  </tr>
										  <tr>
													<td width="2%">&nbsp;</td>
													<td valign="top" colspan="3">
															
															<table width="100%" height="144" border="0" cellpadding="0" cellspacing="0" class="line_table">
																	  <tr>
																				<td  height="27" background="__PUBLIC__/images/news-title-bg.gif">
																							<img src="__PUBLIC__/images/news-title-bg.gif" width="2" height="27">
																				</td>
																				<td width="93%" background="__PUBLIC__/images/news-title-bg.gif" class="left_bt2">会员信息
																				</td>
																				<td  height="27" background="__PUBLIC__/images/news-title-bg.gif">
																							<img src="__PUBLIC__/images/news-title-bg.gif" width="2" height="27">
																				</td>
																	  </tr>
																	   <tr>
																				<td valign="top">&nbsp;</td>
																				<td valign="top" align="center">
																						<form action="__URL__/searchMem" method="post">
																						请输入学号：<input type="text" name="member_id">&nbsp;
																						<input type="submit" name="submit" value="搜索">
																						</form>
																				</td>
																				<td valign="top">&nbsp;</td>
																	  </tr>
																	  <?php if($member != ''): ?><tr>
																						<td height="102" valign="top">&nbsp;</td>
																						<td height="102" valign="top">
																						<table width="100%" border="1px" cellspacing="0px">
																								<tr>
																										<th>id号</th>
																										<th>学号</th>
																										<th>姓名</th>
																										<th>联系方式</th>
																										<th>注册时间</th>
																										<th>学院</th>
																										<th>备注</th>
																										<th>正常签到次数</th>
																										<th>迟到次数</th>
																										<th>旷课次数</th>
																										<th>领卡</th>
																								</tr>
																								<tr align="center">
																								<?php if(is_array($member)): foreach($member as $key=>$col): if($key == 'member_college'): if($col == 1): ?><td align="center">软件学院</td>
																												<?php else: ?>
																															<td align="center">非软件学院</td><?php endif; ?>	
																										<?php elseif($key == 'member_time'): ?>
																												<td align="center"><?php echo (date("Y-m-d H:i:s",$col)); ?></td>
																										<?php elseif($key == 'member_receive_card'): ?>
																												<?php if($col == '0'): ?><td align="center"><font color="red">否</font></td>
																												<?php else: ?>
																														<td align="center">是</td><?php endif; ?>
																										<?php else: ?>
																												<td align="center"><?php echo ($col); ?></td><?php endif; endforeach; endif; ?>
																						</tr>
																						</foreach>
																								</tr>
																				        </table>
																						</td>
																						<td valign="top">&nbsp;</td>
																			  </tr>
																			  <?php if($member["member_receive_card"] == '0'): ?><tr align="center">
																					<td colspan="3">
																							<form action="__URL__/setCard" method="post">
																							请输入内部id：<input type="text" name="member_inside_id">
																							<input type="hidden" name="member_id" value="<?php echo ($member["member_id"]); ?>">
																							<input type="submit" name="submit" value="领卡">
																							</form>
																					</td>
																			   </tr><?php endif; ?>
																			 <?php else: ?>
																			 <h3 align="center"><font color="red"><?php echo ($error); ?></font></h3><?php endif; ?>
															</table>
													</td>
													<td width="2%">&nbsp;</td>
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