let packages = new Map()

// Reset the map every 5 minutes to keep prices updated
setInterval(() => {
  packages = new Map()
}, 300000);

exports.addpackage = (package_id, package_object) => {
  packages.set(package_id, package_object);
}

exports.haspackage = (package_id) => {
  return packages.has(package_id);
}

exports.getpackage = (package_id) => {
  return packages.get(package_id);
}
