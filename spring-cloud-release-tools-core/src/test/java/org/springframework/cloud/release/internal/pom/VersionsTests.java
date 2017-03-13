/*
 *  Copyright 2013-2017 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package org.springframework.cloud.release.internal.pom;

import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.junit.Test;

import static org.assertj.core.api.BDDAssertions.then;

/**
 * @author Marcin Grzejszczak
 */
public class VersionsTests {

	Versions versions = new Versions("", projects());

	@Test
	public void should_add_boot_to_versions_when_version_is_created() {
		then(new Versions("1.2.3.RELEASE").projects)
				.contains(
						new Project("spring-boot", "1.2.3.RELEASE"),
						new Project("spring-boot-starter-parent", "1.2.3.RELEASE")
						);
	}

	@Test
	public void should_return_true_when_project_is_on_the_list() {
		then(this.versions.shouldBeUpdated("foo")).isTrue();
	}

	@Test
	public void should_return_true_when_project_has_a_parent_suffix_and_project_is_on_the_list() {
		then(this.versions.shouldBeUpdated("foo-parent")).isTrue();
	}

	@Test
	public void should_return_false_when_project_is_not_on_the_list() {
		then(this.versions.shouldBeUpdated("missing")).isFalse();
	}

	@Test
	public void should_return_version_for_present_project() {
		then(this.versions.versionForProject("foo")).isEqualTo("bar");
	}

	@Test
	public void should_return_empty_string_for_missing_project() {
		then(this.versions.versionForProject("missing")).isEmpty();
	}

	@Test
	public void should_return_true_if_properties_contains_project_key() {
		then(this.versions.shouldSetProperty(validProps())).isTrue();
	}

	@Test
	public void should_return_false_if_properties_does_not_contain_project_key() {
		then(this.versions.shouldSetProperty(missingProps())).isFalse();
	}

	@Test
	public void should_return_true_when_all_versions_are_snapshot() {
		then(new Versions("", snapshotProjects()).isSnapshot()).isTrue();
	}

	@Test
	public void should_return_true_when_there_only_release_versions() {
		then(new Versions("1.0.0.RELEASE", releaseProjects()).isSnapshot()).isFalse();
	}

	@Test
	public void should_return_true_when_there_are_mixed_versions() {
		then(new Versions("", mixedProjects()).isSnapshot()).isTrue();
	}

	Set<Project> projects() {
		Set<Project> projects = new HashSet<>();
		projects.add(new Project("foo", "bar"));
		return projects;
	}

	Set<Project> snapshotProjects() {
		Set<Project> projects = new HashSet<>();
		projects.add(new Project("foo", "1.0.0.BUILD-SNAPSHOT"));
		return projects;
	}

	Set<Project> releaseProjects() {
		Set<Project> projects = new HashSet<>();
		projects.add(new Project("foo", "1.0.0.RELEASE"));
		return projects;
	}

	Set<Project> mixedProjects() {
		Set<Project> projects = new HashSet<>();
		projects.add(new Project("foo", "1.0.0.BUILD-SNAPSHOT"));
		projects.add(new Project("fooBar", "1.0.0.RELEASE"));
		return projects;
	}

	Properties validProps() {
		Properties properties = new Properties();
		properties.setProperty("foo.version", "1.0.0");
		return properties;
	}

	Properties missingProps() {
		Properties properties = new Properties();
		properties.setProperty("missing.version", "1.0.0");
		return properties;
	}
}