package com.vue.controller;

import java.io.IOException;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.schema.SchemaResponse.UpdateResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
@Controller
@Scope
public class SolrController {
	@Autowired
	private SolrClient solrClient;
	//从solr服务器上查询返回集合
	@RequestMapping("/getList")
	@ResponseBody
	public String getList() throws SolrServerException, IOException{
		SolrClient client = new HttpSolrClient("http://localhost:8081/solrwebapp");
		QueryResponse resp = client.query("newcore",new SolrQuery("*:*"));
		//查询出来的结果都保存在SolrDocumentList中
		SolrDocumentList sdl = resp.getResults();
		System.out.println("总数"+ sdl.getNumFound() );
		for (SolrDocument sd: sdl){
		//sd.setField("address", "天空之城");	//可以修改对应查询出来的记录
		System.out.println("category = "+sd.get("category")+"--uname = "+sd.get("uname")+"--address = "+sd.get("address"));
		}
		return "执行";
	}
	//向solr服务器上存值
	@RequestMapping("/addList")
	@ResponseBody
	public String addList(String uname) throws SolrServerException, IOException{
		SolrClient client = new HttpSolrClient("http://localhost:8081/solrwebapp");
		 SolrInputDocument doc = new SolrInputDocument();
		
	         doc.setField("name", "22121");
	         doc.setField("catalog_name", "21212");
	         doc.setField("price", "300");
	         doc.setField("description", "21212");
	         doc.setField("picture", "2");
         client.add("newcore", doc);
         client.commit("newcore");
	return "新增加";
	}
	//修改solr服务器上存值
	@RequestMapping("/updateList")
	@ResponseBody	
	public String updateList() throws SolrServerException, IOException{
		SolrClient client = new HttpSolrClient("http://localhost:8081/solrwebapp");
		 SolrInputDocument doc = new SolrInputDocument();
         doc.setField("pid", "45");
         client.add("newcore", doc);
         //client.commit();
         client.commit("newcore");
	return "新增加";
	}
	/**
     * 综合查询: 在综合查询中, 有按条件查询, 条件过滤, 排序, 分页, 高亮显示, 获取部分域信息
     * @return
	 * @throws IOException 
	 * @throws SolrServerException 
     */
	//删除solr服务器上存值
	@RequestMapping("/deletList")
	@ResponseBody
    public String deletList() throws SolrServerException, IOException{
		SolrClient client = new HttpSolrClient("http://localhost:8081/solrwebapp");
		client.deleteByQuery("newcore","id:54937adb-23e3-4a25-9480-e5c4a1d59821");
		client.commit("newcore");
        return "删除";
    }
	//从服务器上查询记录
	@RequestMapping("/serachList")
	@ResponseBody
	public String serachList(String cimage) throws SolrServerException, IOException{
		SolrClient client = new HttpSolrClient("http://localhost:8081/solrwebapp");
		SolrQuery query=new SolrQuery();

		//solr的查询和hibernate的动态sql(hql)很相似
		//query.set("q", "*:*");//设置
		//query.set("start", "0");//设置起始行
		//query.set("rows", "10");//设置行数
		//query.set("sort", "p_id asc");//设置排序规则
		QueryResponse resps =client.query("ceshi",query);
		resps.getResults();//获取返回的查询结果
		QueryResponse resp = client.query("ceshi",new SolrQuery("cimage:"+cimage+""));
		SolrDocumentList sdl = resp.getResults();
		System.out.println("总数"+ sdl.getNumFound());
		for (SolrDocument sd: sdl){
		//sd.setField("address", "天空之城");	//可以修改对应查询出来的记录
		System.out.println("cimage = "+sd.get("cimage"));
		}
		return "查询操作";
	}
	@RequestMapping("/getceshiList")
	@ResponseBody
	public String getceshiList(String query) throws SolrServerException, IOException{
		SolrClient client = new HttpSolrClient("http://localhost:8081/solrwebapp");
		SolrQuery solrQuery=new SolrQuery();
		solrQuery.set("q", "cimage:新");
		solrQuery.set("fq","cname:新图");//只能追加一个条件查询
		QueryResponse response=client.query("ceshi",solrQuery);
		SolrDocumentList sdl=response.getResults();
		for(SolrDocument sd :sdl){
			System.out.println("solrdocument:"+sd);
			System.out.println("多字段查询:cimage+cname"+sd.get("cimage"));
		}
		return "查询成功";
	}
}
