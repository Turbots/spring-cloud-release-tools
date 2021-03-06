/*
 * Copyright 2013-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.cloud.release.internal.sagan;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.BDDMockito;
import org.mockito.Mockito;

import org.springframework.cloud.release.internal.ReleaserProperties;
import org.springframework.cloud.release.internal.pom.ProjectVersion;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

/**
 * @author Marcin Grzejszczak
 */
public class SaganUpdaterTest {

	SaganClient saganClient = Mockito.mock(SaganClient.class);

	ReleaserProperties properties = new ReleaserProperties();

	SaganUpdater saganUpdater = new SaganUpdater(this.saganClient, this.properties);

	@Before
	public void setup() {
		Project project = new Project();
		project.projectReleases.addAll(Arrays.asList(release("1.0.0.RC1"),
				release("1.1.0.BUILD-SNAPSHOT"), release("2.0.0.M4")));
		BDDMockito.given(this.saganClient.getProject(anyString())).willReturn(project);
	}

	private Release release(String version) {
		Release release = new Release();
		release.version = version;
		release.current = true;
		return release;
	}

	@Test
	public void should_not_update_sagan_when_switch_is_off() {
		this.properties.getSagan().setUpdateSagan(false);

		this.saganUpdater.updateSagan("master", version("1.0.0.M1"), version("1.0.0.M1"));

		then(this.saganClient).shouldHaveZeroInteractions();
	}

	@Test
	public void should_update_sagan_for_milestone() {
		this.saganUpdater.updateSagan("master", version("1.0.0.M1"), version("1.0.0.M1"));

		then(this.saganClient).should().updateRelease(BDDMockito.eq("foo"),
				BDDMockito.argThat(withReleaseUpdate("1.0.0.M1",
						"http://cloud.spring.io/spring-cloud-static/foo/{version}/",
						"PRERELEASE")));
	}

	@Test
	public void should_update_sagan_for_rc() {
		this.saganUpdater.updateSagan("master", version("1.0.0.RC1"),
				version("1.0.0.RC1"));

		then(this.saganClient).should().updateRelease(BDDMockito.eq("foo"),
				BDDMockito.argThat(withReleaseUpdate("1.0.0.RC1",
						"http://cloud.spring.io/spring-cloud-static/foo/{version}/",
						"PRERELEASE")));
	}

	private ProjectVersion version(String version) {
		return new ProjectVersion("foo", version);
	}

	@Test
	public void should_update_sagan_from_master() {
		ProjectVersion projectVersion = version("1.0.0.BUILD-SNAPSHOT");

		this.saganUpdater.updateSagan("master", projectVersion, projectVersion);

		then(this.saganClient).should().updateRelease(BDDMockito.eq("foo"),
				BDDMockito.argThat(withReleaseUpdate("1.0.0.BUILD-SNAPSHOT",
						"http://cloud.spring.io/foo/foo.html", "SNAPSHOT")));
	}

	@Test
	public void should_update_sagan_from_release_version() {
		ProjectVersion projectVersion = version("1.0.0.RELEASE");

		this.saganUpdater.updateSagan("master", projectVersion, projectVersion);

		then(this.saganClient).should().deleteRelease("foo", "1.0.0.RC1");
		then(this.saganClient).should().deleteRelease("foo", "1.0.0.BUILD-SNAPSHOT");
		then(this.saganClient).should().updateRelease(BDDMockito.eq("foo"),
				BDDMockito.argThat(withReleaseUpdate("1.0.0.RELEASE",
						"http://cloud.spring.io/spring-cloud-static/foo/{version}/",
						"GENERAL_AVAILABILITY")));
		then(this.saganClient).should().deleteRelease("foo", "1.0.0.BUILD-SNAPSHOT");
		then(this.saganClient).should().updateRelease(BDDMockito.eq("foo"),
				BDDMockito.argThat(withReleaseUpdate("1.0.1.BUILD-SNAPSHOT",
						"http://cloud.spring.io/foo/foo.html", "SNAPSHOT")));
	}

	@Test
	public void should_update_sagan_from_non_master() {
		ProjectVersion projectVersion = version("1.1.0.BUILD-SNAPSHOT");

		this.saganUpdater.updateSagan("1.1.x", projectVersion, projectVersion);

		then(this.saganClient).should(never()).deleteRelease(anyString(), anyString());
		then(this.saganClient).should().updateRelease(BDDMockito.eq("foo"),
				BDDMockito.argThat(withReleaseUpdate("1.1.0.BUILD-SNAPSHOT",
						"http://cloud.spring.io/foo/1.1.x/", "SNAPSHOT")));
	}

	private ArgumentMatcher<List<ReleaseUpdate>> withReleaseUpdate(final String version,
			final String refDocUrl, final String releaseStatus) {
		return argument -> {
			ReleaseUpdate item = argument.get(0);
			return "foo".equals(item.artifactId)
					&& releaseStatus.equals(item.releaseStatus)
					&& version.equals(item.version) && refDocUrl.equals(item.apiDocUrl)
					&& refDocUrl.equals(item.refDocUrl) && item.current;
		};
	}

}
