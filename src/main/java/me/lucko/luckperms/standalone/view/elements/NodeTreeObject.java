package me.lucko.luckperms.standalone.view.elements;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.lucko.luckperms.api.Node;

@AllArgsConstructor
public class NodeTreeObject extends RecursiveTreeObject<NodeTreeObject> {

	@Getter
	private Node node;

}
