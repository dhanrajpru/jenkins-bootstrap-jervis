/*
   Created by Sam Gleske
   Configure Credentials for docker cloud stack in Jenkins.
 */

import com.cloudbees.plugins.credentials.CredentialsScope
import com.cloudbees.plugins.credentials.impl.UsernamePasswordCredentialsImpl
import com.cloudbees.plugins.credentials.SystemCredentialsProvider
import com.cloudbees.plugins.credentials.domains.Domain
import com.cloudbees.plugins.credentials.Credentials


SystemCredentialsProvider system_creds = SystemCredentialsProvider.getInstance()
Boolean foundId=false
system_creds.getCredentials().each{
    if('jenkins-docker-cloud-credentials'.equals(it.getId())) {
        foundId=true
    }
}
if(!foundId) {
    Map<Domain, List<Credentials>> domainCredentialsMap = system_creds.getDomainCredentialsMap()
    UsernamePasswordCredentialsImpl creds =
        new UsernamePasswordCredentialsImpl(CredentialsScope.SYSTEM,
                                            'jenkins-docker-cloud-credentials',
                                            'Jenkins slave docker container credentials.',
                                            'jenkins',
                                            'jenkins')
    domainCredentialsMap[Domain.global()].add(creds)
    system_creds.save()
    println 'Added docker cloud credentials.'
}

/*
   Automatically configure the docker cloud stack in Jenkins.
 */

import com.nirima.jenkins.plugins.docker.DockerCloud
import com.nirima.jenkins.plugins.docker.DockerTemplate
import net.sf.json.JSONArray
import net.sf.json.JSONObject
import org.kohsuke.stapler.StaplerRequest

JSONArray<JSONObject> docker_settings = new JSONArray<JSONObject>()
docker_settings.addAll([
    [
        name: 'docker-local',
        serverUrl: 'http://127.0.0.1:4243',
        containerCapStr: '50',
        connectionTimeout: 5,
        readTimeout: 15,
        credentialsId: '',
        version: '',
        templates: [
            [
                image: 'jervis-docker-jvm:latest',
                labelString: 'docker ubuntu1404 language-groovy language-java language-ruby gemfile env rvm jdk',
                remoteFs: '',
                credentialsId: 'jenkins-docker-cloud-credentials',
                idleTerminationMinutes: '5',
                sshLaunchTimeoutMinutes: '1',
                jvmOptions: '',
                javaPath: '',
                memoryLimit: 0,
                cpuShares: 0,
                prefixStartSlaveCmd: '',
                suffixStartSlaveCmd: '',
                instanceCapStr: '50',
                dnsString: '',
                dockerCommand: '/sbin/my_init',
                volumesString: '',
                volumesFromString: '',
                hostname: '',
                bindPorts: '',
                bindAllPorts: false,
                privileged: false,
                tty: false,
                macAddress: ''
            ]
        ]
    ]
])

def bindJSONToList( Class type, Object src) {
    if(type == DockerTemplate){
        ArrayList<DockerTemplate> r = new ArrayList<DockerTemplate>();
        if (src instanceof JSONObject) {
            JSONObject temp = (JSONObject) src;
            r.add(
                    new DockerTemplate(temp.optString('image'),
                                       temp.optString('labelString'),
                                       temp.optString('remoteFs'),
                                       temp.optString('remoteFsMapping'),
                                       temp.optString('credentialsId'),
                                       temp.optString('idleTerminationMinutes'),
                                       temp.optString('sshLaunchTimeoutMinutes'),
                                       temp.optString('jvmOptions'),
                                       temp.optString('javaPath'),
                                       temp.optInt('memoryLimit'),
                                       temp.optInt('cpuShares'),
                                       temp.optString('prefixStartSlaveCmd'),
                                       temp.optString('suffixStartSlaveCmd'),
                                       temp.optString('instanceCapStr'),
                                       temp.optString('dnsString'),
                                       temp.optString('dockerCommand'),
                                       temp.optString('volumesString'),
                                       temp.optString('volumesFromString'),
                                       temp.optString('environmentsString'),
                                       temp.optString('lxcConfString'),
                                       temp.optString('hostname'),
                                       temp.optString('bindPorts'),
                                       temp.optBoolean('bindAllPorts'),
                                       temp.optBoolean('privileged'),
                                       temp.optBoolean('tty'),
                                       temp.optString('macAddress')
                )
            );
        }
        if (src instanceof JSONArray) {
            JSONArray json_array = (JSONArray) src;
            for (Object o : json_array) {
                if (o instanceof JSONObject) {
                    JSONObject temp = (JSONObject) o;
                    r.add(
                            new DockerTemplate(temp.optString('image'),
                                               temp.optString('labelString'),
                                               temp.optString('remoteFs'),
                                               temp.optString('remoteFsMapping'),
                                               temp.optString('credentialsId'),
                                               temp.optString('idleTerminationMinutes'),
                                               temp.optString('sshLaunchTimeoutMinutes'),
                                               temp.optString('jvmOptions'),
                                               temp.optString('javaPath'),
                                               temp.optInt('memoryLimit'),
                                               temp.optInt('cpuShares'),
                                               temp.optString('prefixStartSlaveCmd'),
                                               temp.optString('suffixStartSlaveCmd'),
                                               temp.optString('instanceCapStr'),
                                               temp.optString('dnsString'),
                                               temp.optString('dockerCommand'),
                                               temp.optString('volumesString'),
                                               temp.optString('volumesFromString'),
                                               temp.optString('environmentsString'),
                                               temp.optString('lxcConfString'),
                                               temp.optString('hostname'),
                                               temp.optString('bindPorts'),
                                               temp.optBoolean('bindAllPorts'),
                                               temp.optBoolean('privileged'),
                                               temp.optBoolean('tty'),
                                               temp.optString('macAddress')
                        )
                    );
                }
            }
        }
        return r;
    }
    if(type == DockerCloud){
        ArrayList<DockerCloud> r = new ArrayList<DockerCloud>();
        if (src instanceof JSONObject) {
            JSONObject temp = (JSONObject) src;
            r.add(
                new DockerCloud(temp.optString('name'),
                                bindJSONToList(DockerTemplate.class, temp.optJSONArray('templates')),
                                temp.optString('serverUrl'),
                                temp.optString('containerCapStr'),
                                temp.optInt('connectTimeout', 5),
                                temp.optInt('readTimeout', 15),
                                temp.optString('credentialsId'),
                                temp.optString('version')
                )
            );
        }
        if (src instanceof JSONArray) {
            JSONArray json_array = (JSONArray) src;
            for (Object o : json_array) {
                if (o instanceof JSONObject) {
                    JSONObject temp = (JSONObject) o;
                    r.add(
                        new DockerCloud(temp.optString('name'),
                                        bindJSONToList(DockerTemplate.class, temp.optJSONArray('templates')),
                                        temp.optString('serverUrl'),
                                        temp.optString('containerCapStr'),
                                        temp.optInt('connectTimeout', 5),
                                        temp.optInt('readTimeout', 15),
                                        temp.optString('credentialsId'),
                                        temp.optString('version')
                        )
                    );
                }
            }
        }
        return r;
    }
}

def req = [
    bindJSONToList: { Class type, Object src ->
        bindJSONToList(type, src)
    }
] as org.kohsuke.stapler.StaplerRequest

if(!Jenkins.instance.clouds.getByName('docker-local')) {
  Jenkins.instance.clouds.addAll(req.bindJSONToList(DockerCloud.class, docker_settings))
  println 'Configured docker cloud.'
}
