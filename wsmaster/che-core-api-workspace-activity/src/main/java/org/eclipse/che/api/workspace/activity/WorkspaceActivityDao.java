/*
 * Copyright (c) 2012-2018 Red Hat, Inc.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   Red Hat, Inc. - initial API and implementation
 */
package org.eclipse.che.api.workspace.activity;

import java.util.List;
import org.eclipse.che.api.core.Page;
import org.eclipse.che.api.core.ServerException;
import org.eclipse.che.api.core.model.workspace.WorkspaceStatus;

/**
 * Data access object for workspaces expiration times.
 *
 * @author Max Shaposhnik (mshaposh@redhat.com)
 */
public interface WorkspaceActivityDao {

  /**
   * Sets workspace expiration time. Any running workspace must prolongate expiration time
   * periodically, otherwise it will be stopped by passing that time.
   *
   * @param expiration expiration object to store
   * @throws ServerException when operation failed
   * @deprecated use {@link #setExpirationTime(String, long)} instead
   */
  @Deprecated
  void setExpiration(WorkspaceExpiration expiration) throws ServerException;

  /**
   * Sets workspace expiration time. Any running workspace must prolong expiration time
   * periodically, otherwise it will be stopped by passing that time.
   *
   * @param workspaceId the id of the workspace
   * @param expirationTime the new expiration time
   * @throws ServerException when operation failed
   */
  void setExpirationTime(String workspaceId, long expirationTime) throws ServerException;

  /**
   * Removes workspace expiration time (basically used on ws stop).
   *
   * @param workspaceId workspace id to remove expiration
   * @throws ServerException when operation failed
   */
  void removeExpiration(String workspaceId) throws ServerException;

  /**
   * Finds workspaces which are passed given expiration time and must be stopped.
   *
   * @param timestamp expiration time
   * @return list of workspaces which expiration time is older than given timestamp
   * @throws ServerException when operation failed
   */
  List<String> findExpired(long timestamp) throws ServerException;

  /**
   * Removes the activity record of the provided workspace.
   *
   * @param workspaceId the id of the workspace
   * @throws ServerException on error
   */
  void removeActivity(String workspaceId) throws ServerException;

  /**
   * Sets the time a workspace has been created.
   *
   * @param workspaceId the id of the workspace
   * @param createdTimestamp the time the workspace was created
   * @throws ServerException on error
   */
  void setCreatedTime(String workspaceId, long createdTimestamp) throws ServerException;

  /**
   * Sets the new timestamp for the workspace entering given status.
   *
   * @param workspaceId the id of the transitioned workspace
   * @param status the new workspace status
   * @param timestamp the time the transition occurred
   * @throws ServerException on error
   */
  void setStatusChangeTime(String workspaceId, WorkspaceStatus status, long timestamp)
      throws ServerException;

  /**
   * Finds workspaces that have been in the provided status since before the provided time.
   *
   * @param timestamp the stop-gap time
   * @param status the status of the workspaces
   * @param maxItems max items on the results page
   * @param skipCount how many items of the result to skip
   * @return the list of workspaces ids that has the the specified status since timestamp
   * @throws ServerException on error
   */
  Page<String> findInStatusSince(
      long timestamp, WorkspaceStatus status, int maxItems, long skipCount) throws ServerException;

  /**
   * Similar to {@link #findInStatusSince(long, WorkspaceStatus, int, long)} but merely provides the
   * caller with count of the workspaces in the given state, not their IDs.
   */
  long countWorkspacesInStatus(WorkspaceStatus status, long timestamp) throws ServerException;

  /**
   * Returns the workspace activity record of the provided workspace.
   *
   * @param workspaceId the id of the workspace
   * @return the workspace activity instance
   * @throws ServerException on error
   */
  WorkspaceActivity findActivity(String workspaceId) throws ServerException;
}
