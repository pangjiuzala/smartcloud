<?php
// 本类由系统自动生成，仅供测试用途
			class IndexAction extends Action {
				public function index(){
							if(isset($_GET['error']) && $_GET['error']==1){
									$str="<script>";
									$str.="alert('注册成功！')";
									$str.="</script>";
							}
							$m=new Model('Members');
							$where['member_college']=1;
							$where['member_remark']="填报成功！";
							$myCollege=$m -> where($where) -> count() <=70 ? $m -> where($where) -> count() : 70;
							$where['member_college']=1;
							$where['member_remark']="预备！";
							$myCollegeOther=$m -> where($where) -> count()<=35 ? $m -> where($where) -> count() : 35;
							$restMyCollegeOther=35-$myCollegeOther;
							$restMyCollege=70-$myCollege;
							$this->assign('mycollege',$myCollege);
							$this->assign('restmycollege',$restMyCollege);
							$this->assign('mycollegeother',$myCollegeOther);
							$this->assign('restmycollegeother',$restMyCollegeOther);

							$where['member_college']=2;
							$where['member_remark']="填报成功！";
							$myCollegeNo=$m -> where($where) -> count()<=30 ? $m -> where($where) -> count() : 30;
							$where['member_college']=2;
							$where['member_remark']="预备！";
							$myCollegeOtherNo=$m -> where($where) -> count()<=15 ? $m -> where($where) -> count() : 15;	
							$restMyCollegeOtherNo=15-$myCollegeOtherNo;
							$restMyCollegeNo=30-$myCollegeNo;
							$this->assign('mycollegeno',$myCollegeNo);
							$this->assign('restmycollegeno',$restMyCollegeNo);
							$this->assign('mycollegeotherno',$myCollegeOtherNo);
							$this->assign('restmycollegeotherno',$restMyCollegeOtherNo);
							$this->assign("str",$str);
							$this->display();
				}
			}
?>