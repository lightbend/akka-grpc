import sbt.Keys._
import sbt._
import sbtprotoc.ProtocPlugin
import sbtprotoc.ProtocPlugin.autoImport.PB

object ProtocJSPlugin extends AutoPlugin {

  override def trigger: PluginTrigger = noTrigger

  override def requires: Plugins = ProtocPlugin

  override def projectSettings: Seq[Def.Setting[_]] =
    inConfig(Compile)(PB.targets += PB.gens.js -> resourceManaged.value / "js")
}
