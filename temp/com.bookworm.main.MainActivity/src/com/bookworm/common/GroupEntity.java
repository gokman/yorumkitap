package com.bookworm.common;

import java.util.ArrayList;
import java.util.List;

public class GroupEntity {
	public String Name;
	public List<GroupItemEntity> GroupItemCollection;

	public GroupEntity()
	{
		GroupItemCollection = new ArrayList<GroupItemEntity>();
	}

	public class GroupItemEntity
	{
		public String Name;
	}
}
