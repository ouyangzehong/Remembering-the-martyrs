using System;
using System.Collections.Generic;
using System.Data;
using System.Linq;
using System.Web;
using System.Web.Services;

namespace DaSaiService
{
    /// <summary>
    /// WebService1 的摘要说明
    /// </summary>
    [WebService(Namespace = "http://oooo.org/")]
    [WebServiceBinding(ConformsTo = WsiProfiles.BasicProfile1_1)]
    [System.ComponentModel.ToolboxItem(false)]
    // 若要允许使用 ASP.NET AJAX 从脚本中调用此 Web 服务，请取消注释以下行。 
    // [System.Web.Script.Services.ScriptService]
    public class WebService1 : System.Web.Services.WebService
    {

        [WebMethod]
        public string HelloWorld()
        {
            return "Hello World";
        }
        //插入祈福
        [WebMethod]
        public bool InsertMessageInfo(string ID, string MessageContent, string CreateTime)
        {
            string sqlstr = "INSERT INTO [usermessage]  (ID,MessageContent,CreateTime) VALUES ('" + ID + "','" + MessageContent + "','" + CreateTime + "')";
            return OperatorDB.ExecCmd(sqlstr);
        }
        //获取所有信息
        [WebMethod]
        public List<string> GetAllMessageInfo()
        {
            string sql = "select * from [usermessage] ";
            DataTable dt = OperatorDB.GetDataTable(sql);
            List<string> list = new List<string>();
            if (dt.Rows.Count > 0)
            {
                for (int i = 0; i < dt.Rows.Count; i++)
                {
                    list.Add(dt.Rows[i]["MessageId"].ToString());
                    list.Add(dt.Rows[i]["ID"].ToString());
                    list.Add(dt.Rows[i]["MessageContent"].ToString());
                    list.Add(dt.Rows[i]["CreateTime"].ToString());
                    
                }
                return list;
            }
            return null;
        }
        //根据ID查询最新的信息
        [WebMethod]
        public List<string> GetMessageInfoByID(string ID)
        {
            string sql = "select * from [usermessage] where ID='" + ID + "'";
            DataTable dt = OperatorDB.GetDataTable(sql);
            List<string> list = new List<string>();
            if (dt.Rows.Count > 0)
            {
                list.Add(dt.Rows[dt.Rows.Count-1]["ID"].ToString());
                list.Add(dt.Rows[dt.Rows.Count - 1]["MessageContent"].ToString());
                list.Add(dt.Rows[dt.Rows.Count - 1]["CreateTime"].ToString());
                return list;
            }
            return null;
        }

        ////////////////////////////////////////////////////////////////////////
        ///////////////////////////////////////////////////////////////////////
        /*个人信息模块*/
        ////////////////////////////////////////////////////////////////////////
        //////////////////////////////////////////////////////////////////////
        ////////////////////////插入数据///////////////////////////////////
        [WebMethod]
        public bool Insertuserinfo(string ID, string PassWord, string PetName, string TelNumber, string HeadImg, string School, string Institute, string Major, string CreateTime, string Remark)
        {
            string sqlstr = "INSERT INTO [UserInfo]  (ID,PassWord,PetName,TelNumber,HeadImg,School,Institute,Major,CreateTime,Remark) VALUES ('" + ID + "','" + PassWord + "','" + PetName + "','" + TelNumber + "','" + HeadImg + "','" + School + "','" + Institute + "','" + Major + "','" + CreateTime + "','" + Remark + "')";
            return OperatorDB.ExecCmd(sqlstr);

        }

        //*************修改数据**********************************************//

        //修改头像
        [WebMethod]//修改头像
        public bool UpdateHeadByID(string ID, string headimg)
        {
            string sql = "";
            sql = "UPDATE UserInfo SET HeadImg ='" + headimg + "'"
                + "where ID='" + ID + "'";

            return OperatorDB.ExecCmd(sql);

        }

        [WebMethod]//修改创建人  也就是昵称
        public bool UpdateNameByID(string ID, string petname)
        {
            string sql = "";
            sql = "UPDATE UserInfo SET petname ='" + petname + "'"
                + "where ID='" + ID + "'";

            return OperatorDB.ExecCmd(sql);
        }

        [WebMethod]//修改电话
        public bool UpdateTelNumberByID(string ID, string telnumber)
        {
            string sql = "";
            sql = "UPDATE UserInfo SET telnumber ='" + telnumber + "'"
                + "where ID='" + ID + "'";

            return OperatorDB.ExecCmd(sql);
        }

        //修改学校
        [WebMethod]//修改学校
        public bool UpdateSchoolByID(string ID, string school)
        {
            string sql = "";
            sql = "UPDATE UserInfo SET school ='" + school + "'"
                + "where ID='" + ID + "'";
            return OperatorDB.ExecCmd(sql);
        }
        //修改学院
        [WebMethod]
        public bool UpdateInstituteByID(string ID, string Institute)
        {
            string sql = "";
            sql = "UPDATE UserInfo SET Institute ='" + Institute + "'"
                + "where ID='" + ID + "'";
            return OperatorDB.ExecCmd(sql);
        }

        //修改专业
        [WebMethod]
        public bool UpdateMajorByID(string ID, string Major)
        {
            string sql = "";
            sql = "UPDATE UserInfo SET Major ='" + Major + "'"
                + "where ID='" + ID + "'";
            return OperatorDB.ExecCmd(sql);
        }


        ///////////////////////////////获取数据////////////////////////////////
        /////ID,PassWord,PetName,TelNumber,HeadImg,School,Institute,Major,CreateTime,Remark
        [WebMethod]
        public List<string> GetUserInfoByUsername(string ID)
        {
            string sql = "select * from [UserInfo] where ID='" + ID + "'";
            DataTable dt = OperatorDB.GetDataTable(sql);
            List<string> list = new List<string>();
            if (dt.Rows.Count > 0)
            {
                list.Add(dt.Rows[0]["ID"].ToString());
                list.Add(dt.Rows[0]["PassWord"].ToString());
                return list;
            }
            return null;
        }
        [WebMethod]
        public List<string> GetUserInfoByID(string ID)
        {
            string sqlstr = "select * from UserInfo where ID ='" + ID + "'";
            DataTable dt = OperatorDB.GetDataTable(sqlstr);
            List<string> list = new List<string>();
            if (dt.Rows.Count > 0)
            {
                for (int i = 0; i < dt.Rows.Count; i++)
                {
                    list.Add(dt.Rows[i]["ID"].ToString());
                    list.Add(dt.Rows[i]["PassWord"].ToString());
                    list.Add(dt.Rows[i]["PetName"].ToString());
                    list.Add(dt.Rows[i]["TelNumber"].ToString());
                    list.Add(dt.Rows[i]["HeadImg"].ToString());

                    list.Add(dt.Rows[i]["School"].ToString());
                    list.Add(dt.Rows[i]["Institute"].ToString());
                    list.Add(dt.Rows[i]["Major"].ToString());
                    list.Add(dt.Rows[i]["CreateTime"].ToString());
                    list.Add(dt.Rows[i]["Remark"].ToString());


                }
                return list;
            }
            return null;
        }
        ////通过账号获取一部分信息
        [WebMethod]
        public List<string> GetsomeUserInfoByID(string ID)
        {
            string sqlstr = "select * from UserInfo where ID ='" + ID + "'";
            DataTable dt = OperatorDB.GetDataTable(sqlstr);
            List<string> list = new List<string>();
            if (dt.Rows.Count > 0)
            {
                for (int i = 0; i < dt.Rows.Count; i++)
                {
                    list.Add(dt.Rows[i]["ID"].ToString());
                    list.Add(dt.Rows[i]["PetName"].ToString());               
                    list.Add(dt.Rows[i]["HeadImg"].ToString());
                    list.Add(dt.Rows[i]["School"].ToString());
                    list.Add(dt.Rows[i]["Institute"].ToString());

                }
                return list;
            }
            return null;
        }
        ////不通过账号获取一部分信息
        [WebMethod]
        public List<string> GetsomeUserInfo()
        {
            string sqlstr = "select * from UserInfo";
            DataTable dt = OperatorDB.GetDataTable(sqlstr);
            List<string> list = new List<string>();
            if (dt.Rows.Count > 0)
            {
                for (int i = 0; i < dt.Rows.Count; i++)
                {
                    list.Add(dt.Rows[i]["ID"].ToString());
                    list.Add(dt.Rows[i]["PetName"].ToString());
                    list.Add(dt.Rows[i]["HeadImg"].ToString());
                    list.Add(dt.Rows[i]["School"].ToString());
                    list.Add(dt.Rows[i]["Institute"].ToString());

                }
                return list;
            }
            return null;
        }
        /////////////////////////删除、、、、、、、、////
        ///
        [WebMethod]
        public bool DeleteUserinfoByID(string ID)
        {
            string sql = "DELETE FROM [dbo].[Userinfo] WHERE ID ='" + ID + " '";
            return OperatorDB.ExecCmd(sql);
        }
        //*********************************三行情诗数据服务*****************************************//
        //插入信息服务
        [WebMethod]
        public bool InsertVerseinfo(string ID, string Tel, string TheOne, string TheTwo, string TheThree, string Adress, string Photostring)
        {
            string sql = "INSERT INTO [dbo].[VerseInfo] (ID,Tel,TheOne,TheTwo,TheThree,Adress,Photostring)  VALUES  ('"
                        + ID + "','"
                        + Tel + "','"
                        + TheOne + "','"
                        + TheTwo + "','"
                        + TheThree + "','"
                        + Adress + "','"
                        + Photostring + "')";
            return OperatorDB.ExecCmd(sql);
        }
        //获取Verse所有信息
        [WebMethod]
        public List<string> GetAllVerseInfo()
        {
            string sqlstr = "select * from VerseInfo";
            DataTable dt = OperatorDB.GetDataTable(sqlstr);
            List<string> list = new List<string>();
            if (dt.Rows.Count > 0)
            {
                for (int i = 0; i < dt.Rows.Count; i++)
                {
                    list.Add(dt.Rows[i]["ID"].ToString());
                    list.Add(dt.Rows[i]["TheOne"].ToString());
                    list.Add(dt.Rows[i]["TheTwo"].ToString());
                    list.Add(dt.Rows[i]["TheThree"].ToString());
                    list.Add(dt.Rows[i]["Photostring"].ToString());
                    list.Add(dt.Rows[i]["Zan"].ToString());

                }
                return list;
            }
            return null;
        }
        //根据ID查询学生照片服务
        [WebMethod]
        public List<string> GetPhotostringByID(string ID)
        {

            string sqlstr = "select Photostring from Verseinfo where ID = '"+ID+"'"; 
            DataTable dt = OperatorDB.GetDataTable(sqlstr);
            List<string> list = new List<string>();
            if (dt.Rows.Count > 0)
            {
                for (int i = 0; i < dt.Rows.Count; i++)
                {
                    list.Add(dt.Rows[i]["Photostring"].ToString());
                }
                return list;
            }
            return null;
        }



    }
}
