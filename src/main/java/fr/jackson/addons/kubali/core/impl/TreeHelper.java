package fr.jackson.addons.kubali.core.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import fr.jackson.addons.kubali.configuration.PropertyProvider;
import fr.jackson.addons.kubali.core.ITreeHelper;
import fr.jackson.addons.kubali.model.TreeNode;
import lombok.Setter;

public class TreeHelper implements ITreeHelper {
	private static final Logger LOG = LoggerFactory.getLogger(TreeHelper.class);
	@Setter
	@Autowired
	private PropertyProvider propertyProvider;

	@Override
	public Optional<TreeNode> findByName(TreeNode root, String criteria) {
		LOG.info("Searching for the node {}", criteria);
		List<String> names = transform(criteria);
		String lookingFor = getActualName(names);
		Queue<TreeNode> queue = new PriorityQueue<>();
		queue.add(root);
		while (!queue.isEmpty()) {
			LOG.debug("looking for the node with the name {}", lookingFor);
			TreeNode front = queue.poll();
			if (front != null) {
				// clear the queue no need to process other graĥ
				if (StringUtils.equalsIgnoreCase(front.getFieldName(), lookingFor)) {
					LOG.info("{} node retrieved : clearing the queue");
					queue.clear();
					lookingFor = getActualName(names);
					LOG.debug("The looked for node update to : {}", lookingFor);
				}
				if (lookingFor == null) {
					LOG.info("The final node is retrieved");
					return Optional.of(front);
				} else {
					LOG.debug("{} element added to queue", front.getChildren());
					queue.addAll(front.getChildren());
				}
			}
		}
		LOG.debug("no node retrieved");
		return Optional.empty();
	}

	@Override
	public List<String> transform(String criteria) {
		if (StringUtils.isEmpty(criteria)) {
			return Collections.emptyList();
		}
		return Arrays.stream(criteria.split(propertyProvider.getFieldSplitator())).collect(Collectors.toList());
	}

	private String getActualName(List<String> names) {
		if (names == null) {
			LOG.error("Trying to get criteria from null list {}", names);
			throw new IllegalStateException("");
		} else if (names.isEmpty()) {
			return null;
		}
		String result = names.get(0);
		names.remove(0);
		return result;
	}

	@Override
	public void markChildrenOfHidden(TreeNode node) {
		LOG.info("marking the children of the node {} with the flag hiddenParent = true", node.getFieldName());
		Queue<TreeNode> queue = new PriorityQueue<>();
		queue.offer(node);
		while (!queue.isEmpty()) {
			TreeNode front = queue.poll();
			if (front != null) {
				LOG.debug("node {} marked with the flag", front.getFieldName());
				front.setParentHidden(true);
				queue.addAll(front.getChildren());
			}
		}
	}
}
