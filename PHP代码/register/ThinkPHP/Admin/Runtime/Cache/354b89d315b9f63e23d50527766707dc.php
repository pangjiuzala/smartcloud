<?php if (!defined('THINK_PATH')) exit();?><html>
				<head>
								<title><%= title %> - 管理页面</title>
								<script language=JavaScript>
								function logout(){
									if (confirm("您确定要退出后台管理系统吗？"))
									top.location = "__APP__/Login/do_loginOut";
									return false;
								}
								</script>
								<script language=JavaScript1.2>
								function showsubmenu(sid) {
									var whichEl = eval("submenu" + sid);
									var menuTitle = eval("menuTitle" + sid);
									if (whichEl.style.display == "none"){
										eval("submenu" + sid + ".style.display=\"\";");
									}else{
										eval("submenu" + sid + ".style.display=\"none\";");
									}
								}
								</script>
								<meta http-equiv="content-type" content="text/html"l;charset="utf-8">
								<script language=JavaScript1.2>
								function showsubmenu(sid) {
									var whichEl = eval("submenu" + sid);
									var menuTitle = eval("menuTitle" + sid);
									if (whichEl.style.display == "none"){
										eval("submenu" + sid + ".style.display=\"\";");
									}else{
										eval("submenu" + sid + ".style.display=\"none\";");
									}
								}
								</script>
								<base target="main">
								<link href="__PUBLIC__/images/skin.css" rel="stylesheet" type="text/css">
				</head>
				<body leftmargin="0" topmargin="0">
							<table width="100%" height="64" border="0" cellpadding="0" cellspacing="0" class="admin_topbg">
									  <tr>
												<td width="61%" height="64"><img src="__PUBLIC__/images/logo.gif" width="262" height="64"></td>
												<td width="39%" valign="top">
															<table width="100%" border="0" cellspacing="0" cellpadding="0">
																	  <tr>
																				<td width="74%" height="38" class="admin_txt">管理员：<b><?php echo ($admin_name); ?></b> 您好,感谢登陆使用！</td>
																				<td width="22%"><a href="#" target="_self" onClick="logout();"><img src="__PUBLIC__/images/out.gif" alt="安全退出" width="46" height="20" border="0"></a></td>
																				<td width="4%">&nbsp;</td>
																	  </tr>
																	  <tr>
																				<td height="19" colspan="3">&nbsp;</td>
																	  </tr>
															</table>
												</td>
									  </tr>
							</table>
				</body>
</html>