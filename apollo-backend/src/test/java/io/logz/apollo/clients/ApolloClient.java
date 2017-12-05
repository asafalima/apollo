package io.logz.apollo.clients;

import com.fasterxml.jackson.core.type.TypeReference;
import io.logz.apollo.auth.User;
import io.logz.apollo.blockers.BlockerDefinition;
import io.logz.apollo.configuration.ApolloConfiguration;
import io.logz.apollo.exceptions.ApolloClientException;
import io.logz.apollo.exceptions.ApolloCouldNotLoginException;
import io.logz.apollo.helpers.Common;
import io.logz.apollo.models.DeployableVersion;
import io.logz.apollo.models.Deployment;
import io.logz.apollo.models.Environment;
import io.logz.apollo.models.Service;
import io.logz.apollo.models.Group;
import io.logz.apollo.notifications.Notification;

import java.io.IOException;
import java.util.List;

/**
 * Created by roiravhon on 11/24/16.
 */
public class ApolloClient {

    private final GenericApolloClient genericApolloClient;
    private final User user;

    public ApolloClient(User user, String plainPassword, ApolloConfiguration apolloConfiguration) {
        this.user = user;
        genericApolloClient = new GenericApolloClient(user.getUserEmail(), plainPassword, apolloConfiguration);
    }

    public void login() throws IOException, ApolloCouldNotLoginException {
        genericApolloClient.login();
    }

    public User getClientUser() {
        return user;
    }

    public List<User> getAllUsers() throws ApolloClientException {
        return genericApolloClient.getResult("/user", new TypeReference<List<User>>(){});
    }

    public Environment addEnvironment(Environment environment) throws ApolloClientException {
        String requestBody = Common.generateJson("name", environment.getName(), "geoRegion", environment.getGeoRegion(),
                "availability", environment.getAvailability(), "kubernetesMaster", environment.getKubernetesMaster(),
                "kubernetesToken", environment.getKubernetesToken(), "kubernetesNamespace", environment.getKubernetesNamespace(),
                "servicePortCoefficient", String.valueOf(environment.getServicePortCoefficient()));

        return genericApolloClient.postAndGetResult("/environment", requestBody, new TypeReference<Environment>(){});
    }

    public Environment getEnvironment(int id) throws ApolloClientException {
        return genericApolloClient.getResult("/environment/" + id, new TypeReference<Environment>(){});
    }

    public List<Environment> getAllEnvironments() throws ApolloClientException {
        return genericApolloClient.getResult("/environment", new TypeReference<List<Environment>>(){});
    }

    public Service addService(Service service) throws ApolloClientException {
        String requestBody = Common.generateJson("name", service.getName(),
                "deploymentYaml", service.getDeploymentYaml(),
                "serviceYaml", service.getServiceYaml());
        return genericApolloClient.postAndGetResult("/service", requestBody, new TypeReference<Service>(){});
    }

    public Service getService(int id) throws ApolloClientException {
        return genericApolloClient.getResult("/service/" + id, new TypeReference<Service>(){});
    }

    public List<Service> getAllServices() throws ApolloClientException {
        return genericApolloClient.getResult("/service", new TypeReference<List<Service>>(){});
    }

    public DeployableVersion addDeployableVersion(DeployableVersion deployableVersion) throws ApolloClientException {
        String requestBody = Common.generateJson("gitCommitSha", deployableVersion.getGitCommitSha(),
                "githubRepositoryUrl", deployableVersion.getGithubRepositoryUrl(),
                "serviceId", String.valueOf(deployableVersion.getServiceId()));

        return genericApolloClient.postAndGetResult("/deployable-version", requestBody, new TypeReference<DeployableVersion>(){});
    }

    public DeployableVersion getDeployableVersion(int id) throws ApolloClientException {
        return genericApolloClient.getResult("/deployable-version/" + id, new TypeReference<DeployableVersion>(){});
    }

    public List<DeployableVersion> getAllDeployableVersions() throws ApolloClientException {
        return genericApolloClient.getResult("/deployable-version/", new TypeReference<List<DeployableVersion>>(){});
    }

    public DeployableVersion getDeployableVersionFromSha(String sha, int serviceId) throws ApolloClientException {
        return genericApolloClient.getResult("/deployable-version/sha/" + sha + "/service/" + serviceId, new TypeReference<DeployableVersion>() {});
    }

    public List<DeployableVersion> getLatestDeployableVersionsByServiceId(int serviceId) throws ApolloClientException {
        return genericApolloClient.getResult("/deployable-version/latest/service/" + serviceId, new TypeReference<List<DeployableVersion>>() {});
    }

    public Deployment addDeployment(Deployment deployment) throws ApolloClientException {
        String requestBody = Common.generateJson("environmentId", String.valueOf(deployment.getEnvironmentId()),
                "serviceId", String.valueOf(deployment.getServiceId()),
                "deployableVersionId", String.valueOf(deployment.getDeployableVersionId()));

        return genericApolloClient.postAndGetResult("/deployment", requestBody, new TypeReference<Deployment>() {});
    }

    public Deployment getDeployment(int id) throws ApolloClientException {
        return genericApolloClient.getResult("/deployment/" + id, new TypeReference<Deployment>() {});
    }

    public List<Deployment> getAllDeployments() throws ApolloClientException {
        return genericApolloClient.getResult("/deployment", new TypeReference<List<Deployment>>() {});
    }

    public Group addGroup(Group group) throws ApolloClientException {
        String requestBody = Common.generateJson("name", String.valueOf(group.getName()),
                "serviceId", String.valueOf(group.getServiceId()),
                "environmentId", String.valueOf(group.getEnvironmentId()),
                "scalingFactor", String.valueOf(group.getScalingFactor()),
                "jsonParams", group.getJsonParams());

        return  genericApolloClient.postAndGetResult("/group", requestBody, new TypeReference<Group>() {});
    }

    public Group getGroup(int id) throws ApolloClientException {
        return genericApolloClient.getResult("/group/" + id, new TypeReference<Group>() {});
    }

    public Group getGroupByName(String name) throws ApolloClientException {
        return genericApolloClient.getResult("/group/" + name, new TypeReference<Group>() {});
    }

    public List<Group> getAllGroups() throws ApolloClientException {
        return genericApolloClient.getResult("/group", new TypeReference<List<Group>>() {});
    }

    public Group updateScalingFactor(int groupId, int scalingFactor) throws ApolloClientException {
        String requestBody = Common.generateJson("id", String.valueOf(groupId), "scalingFactor", String.valueOf(scalingFactor));
        return genericApolloClient.putAndGetResult("/scaling/" + groupId, requestBody, new TypeReference<Group>() {});
    }

    public int getScalingFactor(int groupId) throws ApolloClientException {
        return genericApolloClient.getResult("/scaling/apollo-factor/" + groupId, new TypeReference<Integer>() {});
    }

    public BlockerDefinition getBlockerDefinition(int id) throws ApolloClientException {
        return genericApolloClient.getResult("/blocker-definition/" + id, new TypeReference<BlockerDefinition>() {});
    }

    public List<BlockerDefinition> getAllBlockerDefinitions() throws ApolloClientException {
        return genericApolloClient.getResult("/blocker-definition/", new TypeReference<List<BlockerDefinition>>() {});
    }

    public Notification addNotification(Notification notification) throws ApolloClientException {
        String requestBody = Common.generateJson("name", notification.getName(),
                "environmentId", String.valueOf(notification.getEnvironmentId()),
                "serviceId", String.valueOf(notification.getServiceId()),
                "type", String.valueOf(notification.getType()),
                "notificationJsonConfiguration", notification.getNotificationJsonConfiguration());

        return genericApolloClient.postAndGetResult("/notification", requestBody, new TypeReference<Notification>() {});
    }

    public Notification updateNotification(Notification notification) throws ApolloClientException {
        String requestBody = Common.generateJson("id", String.valueOf(notification.getId()),
                "name", notification.getName(),
                "environmentId", String.valueOf(notification.getEnvironmentId()),
                "serviceId", String.valueOf(notification.getServiceId()),
                "type", String.valueOf(notification.getType()),
                "notificationJsonConfiguration", notification.getNotificationJsonConfiguration());

        return genericApolloClient.putAndGetResult("/notification/" + notification.getId(), requestBody, new TypeReference<Notification>() {});
    }

    public Notification getNotification(int id) throws ApolloClientException {
        return genericApolloClient.getResult("/notification/" + id, new TypeReference<Notification>() {});
    }

    public List<Notification> getAllNotifications() throws ApolloClientException {
        return genericApolloClient.getResult("/notification/", new TypeReference<List<Notification>>() {});
    }
}
