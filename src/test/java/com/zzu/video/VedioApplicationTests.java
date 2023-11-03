package com.zzu.video;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zzu.video.entity.Video;
import com.zzu.video.service.VideoService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = VedioApplication.class)
public class VedioApplicationTests {
	@Autowired
	private VideoService videoService;

	@Test
	public void contextLoads() {
		String jsonString = "[" +
				"    {" +
				"        \"uid\": 1," +
				"        \"cover\": \"http://image.pearvideo.com/main/20181205/10194271-154056-0.png\"," +
				"        \"description\": \"随着AI时代离我们越来越近，我们有必要知道哪些关于AI观点是错误的，修正我们的观念，更好地迎接AI时代的到来。\"," +
				"        \"url\": \"http://video.pearvideo.com/mp4/third/20181205/cont-1487014-10194271-154035-hd.mp4\"," +
				"        \"duration\": 120," +
				"        \"tag\": \"Tec\"" +
				"    }," +
				"    {" +
				"        \"uid\": 1," +
				"        \"cover\": \"http://image2.pearvideo.com/cont/20181121/cont-1479605-11710022.jpg\"," +
				"        \"description\": \"很多人用手机拍出的照片画面很平，缺少对比。这期视频，我会从前期拍摄介绍到后期处理，与你分享如何把照片变得立体有趣。\"," +
				"        \"url\": \"http://video.pearvideo.com/mp4/third/20181121/cont-1479605-11925552-082543-hd.mp4\"," +
				"        \"duration\": 120," +
				"        \"tag\": \"Tec\"" +
				"    }," +
				"    {" +
				"        \"uid\": 1," +
				"        \"cover\": \"http://image.pearvideo.com/cont/20181203/cont-1485498-11729571.jpg\"," +
				"        \"description\": \"改革开放40年以来，中国工业发生了翻天覆地的变化，工业化发展成效显著。中国已经成长为工业大国和制造业大国。\"," +
				"        \"url\": \"http://video.pearvideo.com/mp4/third/20181203/cont-1485498-10008579-093028-hd.mp4\"," +
				"        \"duration\": 120," +
				"        \"tag\": \"Tec\"" +
				"    }," +
				"    {" +
				"        \"uid\": 1," +
				"        \"cover\": \"http://image.pearvideo.com/main/20181116/10159031-100906-0.png\"," +
				"        \"description\": \"11月14日媒体报道，ofo创始人戴威在公司召开全员大会，明确表示ofo不会倒闭，同时承诺用户可以退押金。但是账上没钱，一时半会退不出来。\"," +
				"        \"url\": \"http://video.pearvideo.com/mp4/third/20181116/cont-1476998-10159031-095544-hd.mp4\"," +
				"        \"duration\": 120," +
				"        \"tag\": \"Tec\"" +
				"    }," +
				"    {" +
				"        \"uid\": 1," +
				"        \"cover\": \"http://image.pearvideo.com/main/20181122/11549745-195505-0.png\"," +
				"        \"description\": \"荷兰代尔夫特理工大学开发了新型的昆虫机器人。这款机器人以昆虫的翅膀为灵感，通过四片薄如蝉翼的翅膀的扇动，便可灵活地飞行或悬停。\"," +
				"        \"url\": \"http://video.pearvideo.com/mp4/third/20181122/cont-1480817-11549745-195411-hd.mp4\"," +
				"        \"duration\": 120," +
				"        \"tag\": \"Tec\"" +
				"    }" +
				"]";
		List<Video> list = JSONObject.parseArray(jsonString,Video.class);
		for(Video video:list) {
			videoService.addVideo(video);
		}
	}

}
