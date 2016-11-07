package com.chen.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.BSON;
import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.MongoClient;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

public class Connect 
{
	public static void main(String [] args)
	{
		MongoClient client = new MongoClient("127.0.0.1",27017);
		MongoDatabase db = client.getDatabase("chenDatabase");
		MongoCollection coll = db.getCollection("mycol");
		Map map = new HashMap();
		map.put("name", "laowang");
		map.put("sex","male");
		Document doc = new Document("name","chenchen");
		doc.put("age", 18);
		doc.put("town", "shandong");
		//coll.insertOne(doc);//插入1条
		
		//db.createCollection("test1");//创建集合
		
		List<Document> list = new ArrayList<Document>();
		list.add(new Document("name","chenchen"));
		list.add(new Document("name","lili"));
		list.add(new Document("name","wangwang"));
		//coll.insertMany(list);//插入多条数据
		
		coll.createIndex(new Document("name",-1));//创建索引，name索引字段 1正序，-1降序
		
		//聚合函数
		List<Document> list1 = Arrays.asList(
				new Document("$group",new Document("_id","$name").//name为分组字段，
						append("count", new Document("$sum",1))));
		AggregateIterable<Document> ait = coll.aggregate(list1);
		for(Document d : ait)
		{
			System.out.println(d);
		}
		
		
		//批量修改
		//coll.updateMany(Filters.eq("name", "laowang"), new Document("$set",new Document("age",38)));
//		coll.deleteOne(Filters.eq("name", "laowang")); //删除1条
//		coll.deleteMany(Filters.eq("name", "laowang"));//删除多条
		
		//FindIterable it = coll.find();//查询所有的记录
//		FindIterable it = coll.find(Filters.gt("age", 20));  //gt 大于
//		FindIterable it = coll.find(Filters.and(Filters.gt("age", 20),Filters.eq("name", "lili")));   //and 且 
//		FindIterable it = coll.find().limit(5).skip(10);//分页  limit(pageSize).skip(pageSize x (pageNumber-1))
//		FindIterable it = coll.find(Filters.where("this.name.length > 8"));//where js表达式  效率低
		FindIterable it = coll.find().sort(new Document("name",-1));//排序   name排序字段  1正序，-1倒序
		MongoCursor cursor = it.iterator();
		while(cursor.hasNext())
		{
			System.out.println(cursor.next());
		}
		coll.findOneAndUpdate(Filters.eq("name", "lili1"), new Document("$set",new Document("age",88)));//查找更新
		
		
		
	}

}
