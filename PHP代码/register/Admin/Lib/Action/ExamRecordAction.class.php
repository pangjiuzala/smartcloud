﻿<?php
		class ExamRecordAction extends Action{
				public function index(){
						$m=M("examrecord");
						$field=array('exam_record_id','exam_record_ontime_start_time','exam_record_late_start_time','exam_record_late_end_time','exam_record_ontime_end_time');
						$examRecord=$m-> field($field) -> find();
						$this->assign("examRecord",$examRecord);
						$this->display();
				}
				public function examConfig(){
						$m=M("examrecord");
						$arr=$m ->field(array('exam_record_id')) -> find();
						$last_exam_record_id=$arr['exam_record_id'];
						$this->assign("last_exam_record_id",$last_exam_record_id);
						$this->display();
				}
				public function do_exam(){
						$exam_record_id=$_POST['exam_record_id'];						
						$exam_record_ontime_start_time=mktime($_POST['ontime_start_hour'],$_POST['ontime_start_minute'],0,date("m",time()),date('d',time()),date("Y",time()));
						$exam_record_ontime_end_time=mktime($_POST['ontime_end_hour'],$_POST['ontime_end_minute'],0,date("m",time()),date('d',time()),date("Y",time()));
						$exam_record_late_start_time=mktime($_POST['late_start_hour'],$_POST['late_start_minute'],0,date("m",time()),date('d',time()),date("Y",time()));
						$exam_record_late_end_time=mktime($_POST['late_end_hour'],$_POST['late_end_minute'],0,date("m",time()),date('d',time()),date("Y",time()));
						$m=M("Examrecord");
						$m->create();
						$m -> exam_record_ontime_start_time =$exam_record_ontime_start_time;
						$m -> exam_record_ontime_end_time =$exam_record_ontime_end_time;
						$m -> exam_record_late_start_time =$exam_record_late_start_time;
						$m -> exam_record_late_end_time =$exam_record_late_end_time;
						$where['exam_record_num']=1;
						$res=$m -> where($where ) -> save();
						if($res){
									if($exam_record_id>1){
											$m= new Model("Members");
											$count=$m -> count();
											for($i=0;$i<$count;$i++){
														$condition['member_inside_id']=$i;
														if($arr=$m -> where($condition) -> field(array('member_exam_record','member_cut_class')) ->find()){
																$date=$arr['member_exam_record'];
																if(!$date){
																		$save["member_cut_class"]=$arr['member_cut_class']+1;
																		$wheresave['member_inside_id']=$i;
																		$m -> where($wheresave) -> save($save);
																}else{
																		$wheresave['member_inside_id']=$i;
																		$saveUp['member_exam_record']=0;
																		$m -> where($wheresave) -> save($saveUp);
																}
														}
											}
									}
									
									$this->success("配置成功！");
						}else{
									 $this->error("配置失败！");
						}
				}
		}


?>