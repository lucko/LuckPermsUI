package me.lucko.luckperms.standalone.view.elements;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.lucko.luckperms.common.groups.Group;

@AllArgsConstructor
public class GroupTreeObject extends RecursiveTreeObject<GroupTreeObject> {

	@Getter
	private Group group;
}
