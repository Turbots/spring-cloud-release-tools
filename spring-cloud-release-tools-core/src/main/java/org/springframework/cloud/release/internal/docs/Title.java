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

package org.springframework.cloud.release.internal.docs;

import java.util.Objects;

/**
 * @author Marcin Grzejszczak
 */
public class Title {

	final String lastGaTrainName;

	final String currentGaTrainName;

	final String currentSnapshotTrainName;

	Title(String[] row) {
		int initialIndex = row.length == 4 ? 1 : 2;
		this.lastGaTrainName = row[initialIndex].trim();
		this.currentGaTrainName = row[initialIndex + 1].trim();
		this.currentSnapshotTrainName = row[initialIndex + 2].trim();
	}

	Title(String lastGaTrainName, String currentGaTrainName,
			String currentSnapshotTrainName) {
		this.lastGaTrainName = lastGaTrainName.trim();
		this.currentGaTrainName = currentGaTrainName.trim();
		this.currentSnapshotTrainName = currentSnapshotTrainName.trim();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Title title = (Title) o;
		return Objects.equals(this.lastGaTrainName, title.lastGaTrainName)
				&& Objects.equals(this.currentGaTrainName, title.currentGaTrainName)
				&& Objects.equals(this.currentSnapshotTrainName,
						title.currentSnapshotTrainName);
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.lastGaTrainName, this.currentGaTrainName,
				this.currentSnapshotTrainName);
	}

	public String getLastGaTrainName() {
		return this.lastGaTrainName;
	}

	public String getCurrentGaTrainName() {
		return this.currentGaTrainName;
	}

	public String getCurrentSnapshotTrainName() {
		return this.currentSnapshotTrainName;
	}

}
